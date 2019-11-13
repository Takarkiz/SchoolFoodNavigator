package com.takhaki.schoolfoodnavigator

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import java.util.*

class AddShopViewModel : ViewModel() {

    val shopName = MutableLiveData<String>().apply { value = "" }
    val genreTitle = MutableLiveData<String>().apply { value = "" }
    var isVisibleFinishButton = MediatorLiveData<Boolean>().apply { value = false }
    var isVisibleDeleteButton = MediatorLiveData<Boolean>().apply { value = false }

    init {
        val textObserver = Observer<String> {
            val shopTitle = shopName.value ?: ""
            val genre = genreTitle.value ?: ""
            isVisibleFinishButton.value = !shopTitle.isBlank() && !genre.isBlank()
        }
        isVisibleFinishButton.addSource(shopName, textObserver)
        isVisibleFinishButton.addSource(genreTitle, textObserver)
    }


    fun onSendShopInfo() {

        val shop = ShopEntity(
            shopName = shopName.value!!,
            genre = genreTitle.value!!,
            authorId = "111",
            registerDate = Date(),
            lastEditedAt = Date(),
            images = listOf()
        )

        val shopInfoRepository = ShopInfoRepository()
        shopInfoRepository.registrateShop(shop) {
            Log.d("firebase", it.getOrNull().toString())
        }
//
//        shopInfoRepository.loadAllShops { result ->
//            if (result.isSuccess) {
//                val values = result.getOrNull()?.value
//                values?.let {
//                    it.forEach { value ->
//                        Log.d("firebaseFetch", value.toString())
//                    }
//                }
//            }
//        }
    }

}