package com.takhaki.schoolfoodnavigator.mainList

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.takhaki.schoolfoodnavigator.DefaultSetting
import com.takhaki.schoolfoodnavigator.Model.ShopEntity
import com.takhaki.schoolfoodnavigator.Repository.AssessmentRepository
import com.takhaki.schoolfoodnavigator.Repository.ShopInfoRepository
import com.takhaki.schoolfoodnavigator.Repository.UserAuth
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference

class ShopListViewModel(
    application: Application,
    private val navigator: ShopListNavigatorAbstract
) : ShopListViewModelBase(application) {

    // ShopListViewModelContract

    override val shopItemLists: LiveData<List<List<ShopListItemModel>>>
        get() = _shopItems

    override fun activity(activity: AppCompatActivity) {
        navigator.weakActivity = WeakReference(activity)
    }

    override fun didTapAddFabIcon() {
        navigator.toAddShopView()
    }

    override fun didTapOwnProfileIcon(id: String) {
        navigator.toProfilePage(id)
    }

    override fun didTapShopItem(id: String, name: String) {
        navigator.toShopDetail(id, name)
    }

    override fun putTabNumber(num: Int) {
        number = num
    }

    // LifecycleObserver

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        subscribeShopList()
    }

    // AndroidViewModel
    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    // Private

    private var number: Int = 0
    private var appContext: Context = getApplication<DefaultSetting>()
    private val _shopItems = MutableLiveData<List<List<ShopListItemModel>>>()

    // 内部のみで保持しているshopItems
    private val shopItemsByDate = mutableListOf<ShopListItemModel>()
    private val shopItemsByValue = mutableListOf<ShopListItemModel>()
    private val shopItemsByOnlyFav = mutableListOf<ShopListItemModel>()

    private val disposable: CompositeDisposable = CompositeDisposable()

    private fun subscribeShopList() {
        val repository = ShopInfoRepository(appContext)
        repository.fetchAllShops()
            .subscribeBy(
                onSuccess = {
                    it.forEach { shop ->
                        getShopAssessments(shop)
                    }
                },
                onError = {

                }
            ).addTo(disposable)
    }

    private fun getShopAssessments(shop: ShopEntity) {

        val repository = AssessmentRepository(shop.id, appContext)
        repository.fetchAllAssesment()
            .observeOn(Schedulers.computation())
            .subscribeBy(
                onSuccess = { assessments ->
                    val totalScore = assessments.map { assessment ->
                        (assessment.good + assessment.cheep + assessment.distance) / 3
                    }.average()
                    val auth = UserAuth(appContext)
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

                        addShopItem(shopItem, index = 0)
                        shopItemsByDate.sortBy { it.editedAt }

                        addShopItem(shopItem, index = 1)
                        shopItemsByValue.sortByDescending { it.score }

                        // お気に入りのみを表示
                        if (shopItem.isFavorite) addShopItem(shopItem, index = 2)
                        _shopItems.value =
                            listOf(shopItemsByDate, shopItemsByValue, shopItemsByOnlyFav)
                    }
                },
                onError = {

                }
            ).addTo(disposable)
    }

    private fun addShopItem(shopItem: ShopListItemModel, index: Int) {
        when (index) {
            0 -> {
                shopItemsByDate.forEach { item ->
                    if (item.id == shopItem.id) {
                        return
                    }
                }
                shopItemsByDate.add(shopItem)
            }
            1 -> {
                shopItemsByValue.forEach { item ->
                    if (item.id == shopItem.id) {
                        return
                    }
                }
                shopItemsByValue.add(shopItem)
            }
            2 -> {
                shopItemsByOnlyFav.forEach { item ->
                    if (item.id == shopItem.id) {
                        return
                    }
                }
                shopItemsByOnlyFav.add(shopItem)
            }
        }

    }
}