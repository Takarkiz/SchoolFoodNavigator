package com.takhaki.schoolfoodnavigator.Repository

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.storage.FirebaseStorage
import com.takhaki.schoolfoodnavigator.Model.ShopEntity
import com.takhaki.schoolfoodnavigator.Utility.getFileName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.util.*

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

        val shopId = UUID.randomUUID().toString()

        if (imageUri == null) {

            val data = inverseMapping(shop, null)

            shopDB.document(shopId).set(data)
                .addOnSuccessListener {
                    handler(Result.success(shopId))
                }
                .addOnFailureListener { error ->
                    handler(Result.failure(error))
                }
        } else {

            photoUpload(shopId, imageUri, context) { photoResult ->
                if (photoResult.isSuccess) {

                    photoResult.getOrNull()?.let { filePath ->
                        val data = inverseMapping(shop, filePath)

                        shopDB.document(shopId).set(data)
                            .addOnSuccessListener {
                                handler(Result.success(shopId))
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


    // 全てのショップ情報を取得する
    suspend fun getAllShops(): List<ShopEntity> = withContext(Dispatchers.IO) {

        fetchAllShops()

    }

    private fun fetchAllShops(): List<ShopEntity> {
        val shoplist = mutableListOf<ShopEntity>()

        shopDB.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.getResult()?.let { result ->
                    for (doc in result) {
                        shoplist.add(mappingShop(doc))
                    }
                }
            }
        }

        return shoplist
    }

    // IDから一つのショップ情報を取得する
    fun loadShop(shopID: String, result: (Result<LiveData<ShopEntity>>) -> Unit) {

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
        Log.d("imageUrl", imageUri.toString())
        val shopImageRef = storage.reference.child(filePath)

        val stream = FileInputStream(File(imageUri.path!!))
        val uploadTask = shopImageRef.putStream(stream)
        uploadTask.addOnFailureListener { error ->
            handler(Result.failure(error))

        }.addOnSuccessListener {
            handler(Result.success(filePath))
        }

    }

    private fun mappingShop(queryDoc: QueryDocumentSnapshot): ShopEntity {
        val shopName = queryDoc["name"] as String
        val genre = queryDoc["genre"] as String
        val authorId = queryDoc["userId"] as String
        val createdAt = queryDoc["createdAt"] as Timestamp
        val editedAt = queryDoc["editedAt"] as Timestamp
        val images = queryDoc["images"] as List<String>

        return ShopEntity(
            shopName,
            genre,
            authorId,
            createdAt.toDate(),
            editedAt.toDate(),
            images
        )
    }

    private fun inverseMapping(shop: ShopEntity, shopImagePath: String?): Map<String, Any> {

        val shopImages = shop.images.toMutableList()
        shopImagePath?.let {
            shopImages.add(it)
        }

        return mapOf(
            "name" to shop.shopName,
            "genre" to shop.genre,
            "userId" to shop.authorId,
            "createdAt" to shop.registerDate,
            "editedAt" to shop.lastEditedAt,
            "images" to shopImages.toList()
        )
    }

}