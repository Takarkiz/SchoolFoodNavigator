package com.takhaki.schoolfoodnavigator.screen.addShop.view_model

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.takhaki.schoolfoodnavigator.entity.ShopEntity
import com.takhaki.schoolfoodnavigator.repository.FirestorageRepository
import com.takhaki.schoolfoodnavigator.repository.ShopRepositoryContract
import com.takhaki.schoolfoodnavigator.repository.StorageTypes
import com.takhaki.schoolfoodnavigator.repository.UserRepositoryContract
import com.takhaki.schoolfoodnavigator.screen.addShop.AddShopNavigatorAbstract
import com.takhaki.schoolfoodnavigator.screen.addShop.AddShopViewModelBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.*

class AddShopViewModel(
    application: Application,
    private val shopRepository: ShopRepositoryContract,
    private val userRepository: UserRepositoryContract,
    private val navigator: AddShopNavigatorAbstract
) : AddShopViewModelBase(application) {

    override val shopName = MutableLiveData<String>().apply { value = "" }
    override val genreTitle = MutableLiveData<String>().apply { value = "" }

    override val isVisibleFinishButton = MediatorLiveData<Boolean>().apply { value = false }
    val isVisibleDeleteButton = MediatorLiveData<Boolean>().apply { value = false }
    override val shopImageUri = MediatorLiveData<Uri>().apply { value = null }

    override val isVisibleLoading: MutableLiveData<Boolean> get() = _isVisibleLoading

    override fun activity(activity: AppCompatActivity) {
        navigator.weakActivity = WeakReference(activity)
    }

    init {
        val textObserver = Observer<String> {
            val shopTitle = shopName.value ?: ""
            val genre = genreTitle.value ?: ""
            isVisibleFinishButton.value = !shopTitle.isBlank() && !genre.isBlank()
        }
        isVisibleFinishButton.addSource(shopName, textObserver)
        isVisibleFinishButton.addSource(genreTitle, textObserver)
    }


    override fun uploadShopInfo() {
        _isVisibleLoading.postValue(true)

        val userID = userRepository.currentUser?.uid ?: return
        val id = UUID.randomUUID().toString()

        val shop = ShopEntity(
            id = id,
            name = shopName.value!!,
            genre = genreTitle.value!!,
            userID = userID,
            registerDate = Date(),
            lastEditedAt = Date(),
            images = listOf()
        )

        val storage = FirestorageRepository()
        shopImageUri.value?.let { resourceUri ->
            storage.uploadImage(
                id = shop.id,
                imageUri = resourceUri,
                filePathScheme = StorageTypes.SHOP,
                context = appContext
            ) { imageResult ->
                if (imageResult.isSuccess) {
                    viewModelScope.launch {
                        try {
                            withContext(Dispatchers.Default) {
                                shopRepository.registerShop(shop, imageResult.getOrNull())
                            }
                            shopId = id
                            userRepository.addPointShop()

                        } catch (e: Throwable) {
                            Timber.e(e)
                        }
                    }
                    _isVisibleLoading.postValue(false)
                }
            }
        }
    }

    override fun willMoveToAssessment() {
        navigator.toAssessment(shopId, shopName.value ?: "")
    }

    override fun backToShopList() {
        navigator.backToHome()
    }

    override fun deletePhoto() {
        isVisibleDeleteButton.value = false
        shopImageUri.value = null
    }

    // Private
    private var shopId: String = ""

    private val _isVisibleLoading = MutableLiveData<Boolean>().apply { value = false }

    private val appContext: Context get() = getApplication()

}