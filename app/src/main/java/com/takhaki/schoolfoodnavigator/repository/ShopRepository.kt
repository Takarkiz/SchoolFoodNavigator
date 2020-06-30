package com.takhaki.schoolfoodnavigator.repository

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.takhaki.schoolfoodnavigator.entity.ShopEntity
import io.reactivex.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

class ShopRepository(context: Context) : ShopRepositoryContract {


    @ExperimentalCoroutinesApi
    override fun getShops(): Flow<List<ShopEntity>> = callbackFlow {
        val query = shopDB.orderBy("editedAt", Query.Direction.DESCENDING)
        val task = query.addSnapshotListener { snapshot, error ->

            if (error != null) {
                Timber.e(error, "Observe Shops")
                return@addSnapshotListener
            }

            if (snapshot == null) {
                return@addSnapshotListener
            }

            val shops = snapshot.documents.mapNotNull {
                val shop = it.toObject(ShopEntity::class.java)
                shop?.toEntity()
            }
            offer(shops)
        }

        awaitClose { task.remove() }
    }

    override fun registerShop(
        shop: ShopEntity,
        imageUrl: String?
    ): Single<Unit> {
        return Single.create { emitter ->
            val data = inverseMapping(shop, imageUrl)

            shopDB.document(shop.id).set(data)
                .addOnSuccessListener {
                    emitter.onSuccess(Unit)
                }
                .addOnFailureListener { error ->
                    emitter.onError(error)
                }
        }

    }

    @ExperimentalCoroutinesApi
    override fun shop(id: String): Flow<ShopEntity> = callbackFlow {

        val ref = shopDB.document(id)
        val reg = ref.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Timber.e(error, ref.path)
                return@addSnapshotListener
            }

            if (snapshot == null) {
                Timber.w("%s is missing", ref.path)
                return@addSnapshotListener
            }

            val shop = snapshot.toObject(ShopEntity::class.java)?: return@addSnapshotListener
            offer(shop)

        }
        awaitClose { reg.remove() }
    }

    override fun updateEditedDate(shopId: String) {
        shopDB.document(shopId)
            .update("editedAt", FieldValue.serverTimestamp())
            .addOnFailureListener {
                Timber.e(it)
            }
    }

    override fun updateScore(id: String, score: Float) {

    }

    // お店情報の削除
    override fun deleteShop(id: String, handler: (Result<String>) -> Unit) {
        shopDB.document(id)
            .delete()
            .addOnSuccessListener { handler(Result.success("Success")) }
            .addOnFailureListener {
                Timber.e(it)
                handler(Result.failure(it))
            }
    }

    fun updateShop() {

    }


    private val shopDB: CollectionReference

    init {
        val companyRepository = CompanyRepository(context)
        val id = companyRepository.companyId
        shopDB = FirebaseFirestore.getInstance().collection("Team").document(id.toString())
            .collection("Shops")
    }

    //private fun assessmentColRef(id: String) = shopDB.document(id).collection("comment")


    private fun inverseMapping(shop: ShopEntity, shopImagePath: String?): Map<String, Any> {

        val shopImages = shop.images.toMutableList()
        shopImagePath?.let {
            shopImages.add(it)
        }

        return mapOf(
            "id" to shop.id,
            "name" to shop.name,
            "genre" to shop.genre,
            "userId" to shop.userID,
            "createdAt" to shop.registerDate,
            "editedAt" to shop.lastEditedAt,
            "images" to shopImages.toList()
        )
    }


}