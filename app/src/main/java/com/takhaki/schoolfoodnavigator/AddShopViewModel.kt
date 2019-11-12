package com.takhaki.schoolfoodnavigator

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class AddShopViewModel : ViewModel() {

    private val _shopName = MutableLiveData<String>()
    val shopName: LiveData<String>
        get() = _shopName

    private val _genreTitle = MutableLiveData<String>()
    val genreTitle: LiveData<String>
        get() = _genreTitle

    fun onSendShopInfo() {
        val shop = ShopEntity(
            shopName = "コンビニ",
            genre = "販売店",
            authorId = "111",
            registerDate = Date(),
            lastEditedAt = Date()
        )

        val shopInfoRepository = ShopInfoRepository()
        shopInfoRepository.registrateShop(shop) {
            Log.d("firebase", it.getOrNull().toString())
        }

        shopInfoRepository.loadAllShops()
    }

}