package com.takhaki.schoolfoodnavigator.mainList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.takhaki.schoolfoodnavigator.Repository.ShopInfoRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ShopListViewModel : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main

    val shopItemList = MutableLiveData<List<ShopListItemModel>>()


    override fun onCleared() {
        super.onCleared()
        coroutineContext.cancelChildren()
    }

    fun loadListShopItem() {
        launch {
            val repository = ShopInfoRepository()

            shopItemList.value = repository.getAllshoListModel().map { shop ->
                ShopListItemModel(
                    id = "",
                    name = shop.shopName,
                    shopGenre = shop.genre,
                    imageUrl = shop.images[0],
                    score = 4.0f
                )
            }
        }


    }
}