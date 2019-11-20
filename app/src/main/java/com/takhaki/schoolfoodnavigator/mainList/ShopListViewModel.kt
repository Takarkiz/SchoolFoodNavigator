package com.takhaki.schoolfoodnavigator.mainList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.takhaki.schoolfoodnavigator.Model.ShopEntity
import com.takhaki.schoolfoodnavigator.Repository.ShopInfoRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlin.coroutines.CoroutineContext

class ShopListViewModel : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main

    val shopItemList: LiveData<List<ShopListItemModel>>
        get() = _shopItems

    val shops: LiveData<List<ShopEntity>>
        get() = _shops


    override fun onCleared() {
        super.onCleared()
        coroutineContext.cancelChildren()
    }

    private val _shops = MutableLiveData<List<ShopEntity>>()
    private val _shopItems = MutableLiveData<List<ShopListItemModel>>()

    private val disposable: CompositeDisposable = CompositeDisposable()

    fun loadListShopItem() {
        val repository = ShopInfoRepository()
        repository.fetchAllShops()
            .subscribeBy(
                onSuccess = {
                    _shops.value = it
                },
                onError = {

                }
            ).addTo(disposable)

    }
}