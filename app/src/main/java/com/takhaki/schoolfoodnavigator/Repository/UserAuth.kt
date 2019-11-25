package com.takhaki.schoolfoodnavigator.Repository

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.takhaki.schoolfoodnavigator.Model.UserEntity

class UserAuth {

    private val auth: FirebaseAuth
    private val userDB = FirebaseFirestore.getInstance().collection("User")


    val currentUser: FirebaseUser?
        get() = auth.currentUser

    init {
        auth = FirebaseAuth.getInstance()
    }


    fun createUser(
        name: String,
        iconUri: Uri?,
        context: Context,
        handler: (Result<String>) -> Unit
    ) {
        signInAnonymousUser { result ->
            if (result.isSuccess) {
                result.getOrNull()?.let { uid ->
                    createUserAccount(uid, name, iconUri, context) { result ->
                        if (result.isSuccess) {
                            result.getOrNull()?.let {
                                handler(Result.success(it))
                            }
                        } else {
                            result.exceptionOrNull()?.let { e ->
                                handler(Result.failure(e))
                            }
                        }
                    }
                }
            }
        }
    }

    fun fetchUser(uid: String, handler: (Result<UserEntity>) -> Unit) {

        userDB.document(uid).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.let { result ->
                    val user = UserEntity(
                        id = result["id"].toString(),
                        name = result["name"].toString(),
                        profImageUrl = result["iconUrl"].toString(),
                        navScore = result["score"].toString().toLong().toInt(),
                        favList = result["favList"] as List<String>
                    )

                    handler(Result.success(user))
                }
            }
        }.addOnFailureListener { e ->
            handler(Result.failure(e))
        }
    }

    fun currentUserIconUrl(handler: (Result<StorageReference>) -> Unit) {

        val uid = currentUser?.uid ?: return

        userDB.document(uid).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.let {
                    val path = it["iconUrl"].toString()
                    val storage = FirestorageRepository("User")
                    handler(Result.success(storage.getGSReference(path)))
                }
            }
        }.addOnFailureListener { e ->
            handler(Result.failure(e))
        }
    }

    // お気に入りに追加する
    fun addFavoriteShop(shopID: String, handler: (Boolean) -> Unit) {
        val uid = currentUser?.uid ?: return
        userDB.document(uid).update("favList", FieldValue.arrayUnion(shopID))
            .addOnCompleteListener {
                handler(true)
            }
            .addOnFailureListener {
                handler(false)
            }
    }

    // お気に入りから削除する
    fun deleteFavoriteShop(shopID: String, handler: (Boolean) -> Unit) {
        val uid = currentUser?.uid ?: return
        userDB.document(uid).update("favList", FieldValue.arrayRemove(shopID))
            .addOnCompleteListener {
                handler(true)
            }
            .addOnFailureListener {
                handler(false)
            }
    }

    fun addPointShop() {
        val uid = currentUser?.uid ?: return
        userDB.document(uid).update("score", FieldValue.increment(5))
    }

    fun addPointAssessment() {
        val uid = currentUser?.uid ?: return
        userDB.document(uid).update("score", FieldValue.increment(3))
    }

    // ショップIDからそれがユーザーのお気に入りに登録されているかを判断する
    fun checkFavoriteShop(shopID: String, handler: (Boolean) -> Unit) {
        val uid = currentUser?.uid ?: return
        userDB.document(uid).get()
            .addOnSuccessListener {
                it.toObject(UserEntity::class.java)?.let { user ->
                    user.toEntity()
                    if (user.favList.contains(shopID)) {
                        handler(true)
                    } else {
                        handler(false)
                    }
                }
            }.addOnFailureListener {
                handler(false)
            }
    }

    private fun signInAnonymousUser(handler: (Result<String>) -> Unit) {
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.uid?.let { uid ->
                        handler(Result.success(uid))
                    }
                }
            }.addOnFailureListener { e ->
                handler(Result.failure(e))
            }
    }

    private fun createUserAccount(
        uid: String,
        name: String,
        iconUri: Uri?,
        context: Context,
        handler: (Result<String>) -> Unit
    ) {
        val repository = FirestorageRepository("Users")

        iconUri?.let { uri ->
            repository.uploadUserPhoto(uid, uri, context) { result ->
                if (result.isFailure) {
                    result.exceptionOrNull()?.let {
                        handler(Result.failure(it))
                    }
                } else {
                    result.getOrNull()?.let { url ->
                        val user = UserEntity(
                            id = uid,
                            name = name,
                            profImageUrl = url,
                            navScore = 0,
                            favList = listOf()
                        )
                        uploadUserData(user) { result ->
                            if (result.isSuccess) {
                                result.getOrNull()?.let {
                                    handler(Result.success(it))
                                }
                            }
                        }
                    }
                }
            }
        } ?: run {
            // アイコン画像を設定しなかった場合
            val user =
                UserEntity(
                    id = uid,
                    name = name,
                    profImageUrl = null,
                    navScore = 0,
                    favList = listOf()
                )
            uploadUserData(user) { result ->
                if (result.isSuccess) {
                    result.getOrNull()?.let {
                        handler(Result.success(it))
                    }
                }
            }
        }
    }

    private fun uploadUserData(user: UserEntity, handler: (Result<String>) -> Unit) {

        userDB.document(user.id).set(
            mapOf(
                "id" to user.id,
                "name" to user.name,
                "iconUrl" to user.profImageUrl,
                "score" to user.navScore,
                "favList" to user.favList
            )
        ).addOnSuccessListener {
            handler(Result.success(user.id))
        }.addOnFailureListener { e ->
            handler(Result.failure(e))
        }
    }


}