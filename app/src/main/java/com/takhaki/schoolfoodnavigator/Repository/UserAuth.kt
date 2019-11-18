package com.takhaki.schoolfoodnavigator.Repository

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.takhaki.schoolfoodnavigator.Model.UserEntity

class UserAuth {

    private val auth: FirebaseAuth


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
                            favoriteShopList = listOf()
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
                UserEntity(id = uid, name = name, profImageUrl = null, favoriteShopList = listOf())
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
        val userDB = FirebaseFirestore.getInstance().collection("User")
        userDB.document(user.id).set(
            mapOf(
                "id" to user.id,
                "name" to user.name,
                "iconUrl" to user.profImageUrl,
                "favList" to user.favoriteShopList
            )
        ).addOnSuccessListener {
            handler(Result.success(user.id))
        }.addOnFailureListener { e ->
            handler(Result.failure(e))
        }
    }


}