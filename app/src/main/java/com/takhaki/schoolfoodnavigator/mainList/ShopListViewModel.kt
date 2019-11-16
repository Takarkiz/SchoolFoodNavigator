package com.takhaki.schoolfoodnavigator.mainList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.takhaki.schoolfoodnavigator.Model.ShopEntity
import com.takhaki.schoolfoodnavigator.Repository.ShopInfoRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ShopListViewModel : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main

    val shopItemList: LiveData<List<ShopListItemModel>>
        get() = _shopItemList
    private val _shopItemList = MutableLiveData<List<ShopListItemModel>>()


    override fun onCleared() {
        super.onCleared()
        coroutineContext.cancelChildren()
    }

    fun loadListShopItem() {
        launch {
            val repository = ShopInfoRepository()
            try {
                val shopList: List<ShopEntity> = repository.getAllShops()
            }catch (e: CancellationException) {

            }
        }
    }
}