package com.takhaki.schoolfoodnavigator.repository

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageReference
import com.takhaki.schoolfoodnavigator.Model.CompanyData
import com.takhaki.schoolfoodnavigator.Model.UserEntity
import io.reactivex.Single

class UserAuth(context: Context) {

    private val auth = FirebaseAuth.getInstance()
    private val userDB: CollectionReference


    val currentUser: FirebaseUser?
        get() = auth.currentUser

    init {
        val companyID = CompanyData.getCompanyId(context)
        userDB = FirebaseFirestore.getInstance().collection("Team").document(companyID.toString())
            .collection("User")

    }

    fun signInUser(handler: (Result<String>) -> Unit) {
        signInAnonymousUser { result ->
            if (result.isSuccess) {
                result.getOrNull()?.let { uid ->
                    handler(Result.success(uid))
                }
            }
        }
    }


    fun createUser(
        name: String,
        iconUri: Uri?,
        context: Context,
        handler: (Result<String>) -> Unit
    ) {
        val uid = currentUser?.uid ?: return

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

    fun fetchAllUser(): Single<List<UserEntity>> {
        return Single.create { emitter ->
            val query = userDB.orderBy("score", Query.Direction.DESCENDING)
            query.addSnapshotListener { querySnapshot, error ->
                querySnapshot?.documents?.mapNotNull {
                    val member = it.toObject(UserEntity::class.java)
                    member?.toEntity()
                }?.let {
                    emitter.onSuccess(it)
                }

                if (error != null) {
                    emitter.onError(error)
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
                        iconUrl = result["iconUrl"].toString(),
                        score = result["score"].toString().toLong().toInt(),
                        favList = result["favList"] as List<String>
                    )

                    handler(Result.success(user))
                }
            }
        }.addOnFailureListener { e ->
            handler(Result.failure(e))
        }
    }

    fun userNameAndIconPath(userID: String, handler: (Result<Pair<String, String?>>) -> Unit) {
        userDB
            .document(userID)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.let {
                        val path = it["iconUrl"].toString()
                        val name = it["name"].toString()
                        handler(Result.success(Pair(name, path)))
                    }
                }
            }
    }

    fun currentUserIconUrl(handler: (Result<StorageReference>) -> Unit) {

        val uid = currentUser?.uid ?: return

        userDB.document(uid).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.let {
                    val path = it["iconUrl"].toString()
                    handler(Result.success(FirestorageRepository.getGSReference(path)))
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
        val repository = FirestorageRepository()

        iconUri?.let { uri ->
            repository.uploadUserPhoto(uid, uri, StorageTypes.USER, context) { result ->
                if (result.isFailure) {
                    result.exceptionOrNull()?.let {
                        handler(Result.failure(it))
                    }
                } else {
                    result.getOrNull()?.let { url ->
                        val user = UserEntity(
                            id = uid,
                            name = name,
                            iconUrl = url,
                            score = 0,
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
                    iconUrl = null,
                    score = 0,
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
                "iconUrl" to user.iconUrl,
                "score" to user.score,
                "favList" to user.favList
            )
        ).addOnSuccessListener {
            handler(Result.success(user.id))
        }.addOnFailureListener { e ->
            handler(Result.failure(e))
        }
    }


}