package com.takhaki.schoolfoodnavigator.Repository

import android.content.Context
import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.takhaki.schoolfoodnavigator.Model.ShopEntity
import com.takhaki.schoolfoodnavigator.Utility.getFileName
import io.reactivex.Single
import java.io.File
import java.io.FileInputStream

class ShopInfoRepository {

    val shopDB = FirebaseFirestore.getInstance().collection("Shops")
    val storage = FirebaseStorage.getInstance("gs://schoolfoodnavigator.appspot.com")

    // お店の登録->結果としてショップIDを返す
    fun registrateShop(
        shop: ShopEntity,
        imageUri: Uri?,
        context: Context,
        handler: (Result<String>) -> Unit
    ) {

        if (imageUri == null) {

            val data = inverseMapping(shop, null)

            shopDB.document(shop.id).set(data)
                .addOnSuccessListener {
                    handler(Result.success(shop.id))
                }
                .addOnFailureListener { error ->
                    handler(Result.failure(error))
                }
        } else {

            photoUpload(shop.id, imageUri, context) { photoResult ->
                if (photoResult.isSuccess) {

                    photoResult.getOrNull()?.let { filePath ->
                        val data = inverseMapping(shop, filePath)

                        shopDB.document(shop.id).set(data)
                            .addOnSuccessListener {
                                handler(Result.success(shop.id))
                            }
                            .addOnFailureListener { error ->
                                handler(Result.failure(error))
                            }
                    }
                } else {
                    photoResult.exceptionOrNull()?.let { error ->
                        handler(Result.failure(error))
                    }
                }
            }
        }
    }


    // IDから一つのショップ情報を取得する
    fun loadShop(shopID: String): Single<ShopEntity> {
        return Single.create { emitter ->
            shopDB.document(shopID).get()
                .addOnSuccessListener { snapshot ->
                    val shop = snapshot.toObject(ShopEntity::class.java)
                    shop?.toEntity()?.let {
                        emitter.onSuccess(it)
                    }
                }
                .addOnFailureListener { e ->
                    emitter.tryOnError(e)
                }
        }
    }

    // お店情報の削除
    fun deleteShop(shopID: String) {

    }

    fun updateShop() {

    }

    private fun photoUpload(
        shopID: String,
        imageUri: Uri,
        context: Context,
        handler: (Result<String>) -> Unit
    ) {
        val fileName = imageUri.getFileName(context) ?: ""
        val filePath = "Shops/${shopID}/${fileName}"
        val shopImageRef = storage.reference.child(filePath)

        val stream = FileInputStream(File(imageUri.path!!))
        val uploadTask = shopImageRef.putStream(stream)
        uploadTask.addOnFailureListener { error ->
            handler(Result.failure(error))

        }.addOnSuccessListener {
            handler(Result.success(filePath))
        }

    }

    fun fetchAllShops(): Single<List<ShopEntity>> {

        return Single.create<List<ShopEntity>> { emitter ->
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