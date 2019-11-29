package com.takhaki.schoolfoodnavigator.mainList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.takhaki.schoolfoodnavigator.Model.ShopEntity
import com.takhaki.schoolfoodnavigator.Repository.AssesmentRepository
import com.takhaki.schoolfoodnavigator.Repository.ShopInfoRepository
import com.takhaki.schoolfoodnavigator.Repository.UserAuth
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ShopListViewModel(application: Application) : AndroidViewModel(application) {

    val shopItemList: LiveData<List<ShopListItemModel>>
        get() = _shopItems

    val shops: LiveData<List<ShopEntity>>
        get() = _shops

    private var number: Int = 0


    private val _shops = MutableLiveData<List<ShopEntity>>()
    private val _shopItems = MutableLiveData<List<ShopListItemModel>>()

    // 内部のみで保持しているshopItems
    private val shopItems = mutableListOf<ShopListItemModel>()

    private val disposable: CompositeDisposable = CompositeDisposable()

    fun loadListShopItem() {
        val repository = ShopInfoRepository(getApplication())
        repository.fetchAllShops()
            .subscribeBy(
                onSuccess = {
                    it.forEach { shop ->
                        getShopItems(shop)
                    }
                },
                onError = {

                }
            ).addTo(disposable)
    }

    private fun getShopItems(shop: ShopEntity) {

        val repository = AssesmentRepository(shop.id, getApplication())
        repository.fetchAllAssesment()
            .observeOn(Schedulers.computation())
            .subscribeBy(
                onSuccess = { assesments ->
                    val totalScore = assesments.map { assessment ->
                        (assessment.good + assessment.cheep + assessment.distance) / 3
                    }.average()
                    val auth = UserAuth(getApplication())
                    auth.checkFavoriteShop(shop.id) { isFavorite ->
                        val shopItem = ShopListItemModel(
                            id = shop.id,
                            name = shop.name,
                            shopGenre = shop.genre,
                            editedAt = shop.lastEditedAt,
                            isFavorite = isFavorite,
                            imageUrl = if (shop.images.isNotEmpty()) shop.images[0] else "",
                            score = totalScore.toFloat()
                        )

                        when (number) {
                            0 -> {
                                addShopItem(shopItem)
                                shopItems.sortBy {
                                    it.editedAt
                                }
                            }
                            1 -> {
                                addShopItem(shopItem)
                                shopItems.sortByDescending {
                                    it.score
                                }
                            }
                            2 -> {
                                // お気に入りのみを表示
                                if (shopItem.isFavorite) addShopItem(shopItem)
                            }
                        }

                        _shopItems.value = shopItems
                    }
                },
                onError = {

                }
            ).addTo(disposable)
    }

    fun putTabNumber(num: Int) {
        number = num
    }

    private fun addShopItem(shopItem: ShopListItemModel) {
        shopItems.forEach { item ->
            if (item.id == shopItem.id) {
                return
            }
        }
        shopItems.add(shopItem)
    }
}