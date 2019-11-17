package com.takhaki.schoolfoodnavigator.Repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UserAuth {

    private val auth: FirebaseAuth

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    init {
        auth = FirebaseAuth.getInstance()
    }

    fun signInAnonymousUser(handler: (Result<String>) -> Unit) {
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
}