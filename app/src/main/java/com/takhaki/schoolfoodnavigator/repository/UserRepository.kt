package com.takhaki.schoolfoodnavigator.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.takhaki.schoolfoodnavigator.entity.UserEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserRepository(context: Context) : UserRepositoryContract {

    private val auth = FirebaseAuth.getInstance()
    private val userDB: CollectionReference

    init {
        val companyRepository = CompanyRepository(context)
        val companyID = companyRepository.companyId
        userDB = FirebaseFirestore.getInstance().collection("Team").document(companyID.toString())
            .collection("User")

    }

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun signInUser(): String = suspendCoroutine { con ->
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.uid?.let { uid ->
                        con.resumeWith(Result.success(uid))
                    }
                }
            }.addOnFailureListener {
                con.resumeWith(Result.failure(it))
            }
    }

    override suspend fun createUser(name: String, iconUrl: String?): Unit =
        suspendCoroutine { con ->
            currentUser?.uid?.let { uid ->
                val user = UserEntity(
                    id = uid,
                    name = name,
                    iconUrl = iconUrl,
                    score = 0,
                    favList = listOf()
                )

                userDB.document(user.id).set(
                    mapOf(
                        "id" to user.id,
                        "name" to user.name,
                        "iconUrl" to user.iconUrl,
                        "score" to user.score,
                        "favList" to user.favList
                    )
                ).addOnSuccessListener {
                    con.resume(Unit)
                }.addOnFailureListener { e ->
                    con.resumeWithException(e)
                }
            }

        }

    @ExperimentalCoroutinesApi
    override fun fetchAllUser(): Flow<List<UserEntity>> = callbackFlow {

        val query = userDB.orderBy("score", Query.Direction.DESCENDING)
        val registration = query.addSnapshotListener { querySnapshot, error ->

            if (error != null) {
                Timber.e(error)
                return@addSnapshotListener
            }

            if (querySnapshot == null) {
                return@addSnapshotListener
            }

            val users = querySnapshot.documents.mapNotNull {
                val member = it.toObject(UserEntity::class.java)
                member?.toEntity()
            }
            offer(users)
        }

        awaitClose { registration.remove() }
    }

    @ExperimentalCoroutinesApi
    override fun fetchUser(uid: String): Flow<UserEntity> = callbackFlow {
        val registration = userDB.document(uid).addSnapshotListener { snapshot, error ->

            if (error != null) {
                Timber.e(error)
                return@addSnapshotListener
            }

            if (snapshot == null) {
                return@addSnapshotListener
            }

            val shop = snapshot.toObject(UserEntity::class.java) ?: return@addSnapshotListener

            offer(shop)
        }

        awaitClose { registration.remove() }
    }

    override suspend fun addFavoriteShop(id: String): Unit = suspendCoroutine { con ->
        currentUser?.uid?.let { uid ->
            userDB.document(uid).update("favList", FieldValue.arrayUnion(id))
                .addOnCompleteListener {
                    con.resume(Unit)
                }
                .addOnFailureListener {
                    con.resumeWithException(it)
                }
        }

    }

    override suspend fun removeFavoriteShop(id: String): Unit = suspendCoroutine { con ->
        currentUser?.uid?.let { uid ->
            userDB.document(uid).update("favList", FieldValue.arrayRemove(id))
                .addOnCompleteListener {
                    con.resume(Unit)
                }
                .addOnFailureListener {
                    con.resumeWithException(it)
                }
        }
    }

    override fun addPointShop() {
        val uid = currentUser?.uid ?: return
        userDB.document(uid).update("score", FieldValue.increment(5))
    }

    override fun addPointAssessment() {
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

}