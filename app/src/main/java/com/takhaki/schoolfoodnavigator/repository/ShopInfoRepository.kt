package com.takhaki.schoolfoodnavigator.repository

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.takhaki.schoolfoodnavigator.model.AssessmentEntity
import com.takhaki.schoolfoodnavigator.model.CompanyData
import com.takhaki.schoolfoodnavigator.model.ShopEntity
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import timber.log.Timber

class ShopInfoRepository(context: Context) : ShopRepositoryContract {


    override fun getShops(): Flowable<List<ShopEntity>> =
        Flowable.create({ emitter ->
            val query = shopDB.orderBy("editedAt", Query.Direction.DESCENDING)
            query.get()
                .addOnSuccessListener { snapshot ->
                    val shops = snapshot.documents.mapNotNull {
                        val shop = it.toObject(ShopEntity::class.java)
                        shop?.toEntity()
                    }
                    emitter.onNext(shops)
                }
                .addOnFailureListener { e ->
                    emitter.tryOnError(e)
                }
        }, BackpressureStrategy.LATEST)

    override fun registerShop(
        shop: ShopEntity,
        imageUrl: String?,
        handler: (Result<String>) -> Unit
    ) {
        val data = inverseMapping(shop, imageUrl)

        shopDB.document(shop.id).set(data)
            .addOnSuccessListener {
                handler(Result.success(shop.id))
            }
            .addOnFailureListener { error ->
                handler(Result.failure(error))
            }
    }

    override fun shop(id: String): Flowable<ShopEntity> {
        return shopEntity(id).map { it.toEntity() }
    }

    override fun assessments(id: String): Flowable<List<AssessmentEntity>> {
        return assessments(id).map { reviews ->
            reviews.map { review ->
                review.toEntity()
            }
        }
    }

    override fun updateEditedDate(shopId: String) {
        shopDB.document(shopId)
            .update("editedAt", FieldValue.serverTimestamp())
            .addOnFailureListener {
                Timber.e(it)
            }
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
        val id = CompanyData.getCompanyId(context)
        shopDB = FirebaseFirestore.getInstance().collection("Team").document(id.toString())
            .collection("Shops")
    }

    private fun assessmentColRef(id: String) = shopDB.document(id).collection("comment")


    private fun shopEntity(id: String): Flowable<ShopEntity> =
        Flowable.create({ emitter ->
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

                val shop = snapshot.toObject(ShopEntity::class.java)
                if (shop == null) {
                    Timber.w("%s is missing", ref.path)
                    return@addSnapshotListener
                }

                emitter.onNext(shop)
            }
            emitter.setCancellable { reg.remove() }
        }, BackpressureStrategy.LATEST)

    private fun assessment(id: String): Flowable<List<AssessmentEntity>> {

        return Flowable.create({ emitter ->
            val reg = assessmentColRef(id).addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if (snapshot == null) {
                    return@addSnapshotListener
                }

                val shopAssessment = snapshot.toObjects(AssessmentEntity::class.java)

                emitter.onNext(shopAssessment)
            }
            emitter.setCancellable { reg.remove() }
        }, BackpressureStrategy.LATEST)
    }

    fun fetchAllShops(): Single<List<ShopEntity>> {

        return Single.create { emitter ->
            val query = shopDB.orderBy("editedAt", Query.Direction.DESCENDING)
            query.get()
                .addOnSuccessListener { snapshot ->
                    val shops = snapshot.documents.mapNotNull {
                        val shop = it.toObject(ShopEntity::class.java)
                        shop?.toEntity()
                    }
                    emitter.onSuccess(shops)
                }
                .addOnFailureListener { e ->
                    emitter.tryOnError(e)
                }
        }
    }

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