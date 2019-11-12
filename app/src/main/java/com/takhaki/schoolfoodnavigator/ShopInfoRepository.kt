package com.takhaki.schoolfoodnavigator

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class ShopInfoRepository {

    val shopDB = FirebaseFirestore.getInstance().collection("Shops")


    // お店の登録->結果としてショップIDを返す
    fun registrateShop(shop: ShopEntity, handler: (Result<String>) -> Unit) {

        val data = mapOf(
            "name" to shop.shopName,
            "genre" to shop.genre,
            "userId" to shop.authorId,
            "createdAt" to shop.registerDate,
            "editedAt" to shop.lastEditedAt
        )

        shopDB.add(data)
            .addOnSuccessListener { doc ->
                handler(Result.success(doc.id))
            }
            .addOnFailureListener { error ->
                handler(Result.failure(error))
            }
    }

    // 全てのショップ情報を取得する
    fun loadAllShops() {
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
        //val shopListLiveData: LiveData<List<ShopEntity>> =
    }

//    // IDから一つのショップ情報を取得する
//    fun loadShop(shopID: String): LiveData<ShopEntity> {
//
//    }

    // お店情報の削除
    fun deleteShop(shopID: String) {

    }

    private fun mappingShop(queryDoc: QueryDocumentSnapshot): ShopEntity {
        val shopName = queryDoc["name"] as String
        val genre = queryDoc["genre"] as String
        val authorId = queryDoc["userId"] as String
        val createdAt = queryDoc["createdAt"] as Timestamp
        val editedAt = queryDoc["editedAt"] as Timestamp

        return ShopEntity(shopName, genre, authorId, createdAt.toDate(), editedAt.toDate())
    }
}