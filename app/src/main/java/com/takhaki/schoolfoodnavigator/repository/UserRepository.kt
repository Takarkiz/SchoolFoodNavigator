package com.takhaki.schoolfoodnavigator.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.takhaki.schoolfoodnavigator.entity.UserEntity
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single

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

    override fun signInUser(): Single<String> {
        return Single.create { emitter ->
            auth.signInAnonymously()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        auth.uid?.let { uid ->
                            emitter.onSuccess(uid)
                        }
                    }
                }.addOnFailureListener { e ->
                    emitter.onError(e)
                }
        }
    }

    override fun createUser(name: String, iconUrl: String?): Single<String> {
        return Single.create { emitter ->
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
                    emitter.onSuccess("Success")
                }.addOnFailureListener { e ->
                    emitter.onError(e)
                }
            }
        }
    }

    override fun fetchAllUser(): Flowable<List<UserEntity>> {
        return Flowable.create({ emitter ->
            val query = userDB.orderBy("score", Query.Direction.DESCENDING)
            query.addSnapshotListener { querySnapshot, error ->

                if (error != null) {
                    emitter.onError(error)
                    return@addSnapshotListener
                }

                if (querySnapshot == null) {
                    return@addSnapshotListener
                }

                val users = querySnapshot.documents.mapNotNull {
                    val member = it.toObject(UserEntity::class.java)
                    member?.toEntity()
                }
                emitter.onNext(users)
            }
        }, BackpressureStrategy.BUFFER)
    }

    override fun fetchUser(uid: String): Flowable<UserEntity> {
        return Flowable.create({ emitter ->
            userDB.document(uid).addSnapshotListener { snapshot, error ->

                if (error != null) {
                    emitter.onError(error)
                    return@addSnapshotListener
                }

                if (snapshot == null) {
                    return@addSnapshotListener
                }

                snapshot.toObject(UserEntity::class.java)?.let {
                    emitter.onNext(it)
                }

            }
        }, BackpressureStrategy.LATEST)
    }

    override fun addFavoriteShop(id: String): Single<Unit> {
        return Single.create { emitter ->
            currentUser?.uid?.let { uid ->
                userDB.document(uid).update("favList", FieldValue.arrayUnion(id))
                    .addOnCompleteListener {
                        emitter.onSuccess(Unit)
                    }
                    .addOnFailureListener {
                        emitter.onError(it)
                    }
            }
        }
    }

    override fun removeFavoriteShop(id: String): Single<Unit> {
        return Single.create { emitter ->
            currentUser?.uid?.let { uid ->
                userDB.document(uid).update("favList", FieldValue.arrayRemove(id))
                    .addOnCompleteListener {
                        emitter.onSuccess(Unit)
                    }
                    .addOnFailureListener {
                        emitter.onError(it)
                    }
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