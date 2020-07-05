package com.takhaki.schoolfoodnavigator.screen.mainList.view_model

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.takhaki.schoolfoodnavigator.DefaultSetting
import com.takhaki.schoolfoodnavigator.entity.ShopEntity
import com.takhaki.schoolfoodnavigator.repository.AssessmentRepository
import com.takhaki.schoolfoodnavigator.repository.ShopRepositoryContract
import com.takhaki.schoolfoodnavigator.repository.UserRepository
import com.takhaki.schoolfoodnavigator.repository.UserRepositoryContract
import com.takhaki.schoolfoodnavigator.screen.mainList.ShopListNavigatorAbstract
import com.takhaki.schoolfoodnavigator.screen.mainList.ShopListViewModelBase
import com.takhaki.schoolfoodnavigator.screen.mainList.model.ShopListItemModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class ShopListViewModel(
    application: Application,
    private val shopRepository: ShopRepositoryContract,
    private val userRepository: UserRepositoryContract,
    private val navigator: ShopListNavigatorAbstract
) : ShopListViewModelBase(application) {

    // ShopListViewModelContract

    override val shopItemLists: LiveData<List<List<ShopListItemModel>>> get() = _shopItems
    override val userIconUrl: LiveData<String> get() = _userIconUrl

    override fun activity(activity: AppCompatActivity) {
        navigator.weakActivity = WeakReference(activity)
    }

    override fun didTapAddFabIcon() {
        navigator.toAddShopView()
    }

    override fun didTapOwnProfileIcon() {
        userRepository.currentUser?.uid?.let { id ->
            navigator.toProfilePage(id)
        }
    }

    override fun didTapShopItem(id: String, name: String) {
        navigator.toShopDetail(id, name)
    }

    override fun putTabNumber(index: Int) {
        number = index
    }

    // LifecycleObserver

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        subscribeShopList()
        subscribeCurrentUser()
    }

    // Private

    private var number: Int = 0
    private var appContext: Context = getApplication<DefaultSetting>()
    private val _shopItems = MutableLiveData<List<List<ShopListItemModel>>>()
    private val _userIconUrl = MutableLiveData<String>()

    // 内部のみで保持しているshopItems
    private val shopItemsByDate = mutableListOf<ShopListItemModel>()
    private val shopItemsByValue = mutableListOf<ShopListItemModel>()
    private val shopItemsByOnlyFav = mutableListOf<ShopListItemModel>()

    private fun subscribeShopList() {
        viewModelScope.launch(Dispatchers.Main) {
            shopRepository.getShops()
                .collect {
                    it.forEach { shop ->
                        getShopAssessments(shop)
                    }
                }
        }
    }

    private fun subscribeCurrentUser() {
        viewModelScope.launch(Dispatchers.Main) {
            userRepository.currentUser?.uid?.let {
                userRepository.fetchUser(it)
                    .collect { user ->
                        _userIconUrl.postValue(user.iconUrl)
                    }
            }
        }
    }

    private fun getShopAssessments(shop: ShopEntity) {
        val repository = AssessmentRepository(shop.id, appContext)
        viewModelScope.launch(Dispatchers.Main) {
            repository.fetchAllAssessment().collect { assessments ->
                val totalScore =
                    if (assessments.isNotEmpty()) assessments.map { assessment ->
                        (assessment.good + assessment.cheep + assessment.distance) / 3
                    }.average() else 0.0
                val auth = UserRepository(appContext)
                auth.checkFavoriteShop(shop.id) { isFavorite ->
                    val shopItem =
                        ShopListItemModel(
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
            }
        }
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