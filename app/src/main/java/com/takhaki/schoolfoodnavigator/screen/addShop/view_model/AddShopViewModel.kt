package com.takhaki.schoolfoodnavigator.screen.addShop.view_model

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.takhaki.schoolfoodnavigator.entity.ShopEntity
import com.takhaki.schoolfoodnavigator.repository.FirestorageRepository
import com.takhaki.schoolfoodnavigator.repository.ShopInfoRepository
import com.takhaki.schoolfoodnavigator.repository.StorageTypes
import com.takhaki.schoolfoodnavigator.repository.UserAuth
import com.takhaki.schoolfoodnavigator.screen.addShop.AddShopNavigatorAbstract
import com.takhaki.schoolfoodnavigator.screen.addShop.AddShopViewModelBase
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.*

class AddShopViewModel(
    application: Application,
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

        val auth = UserAuth(getApplication())
        val userID = auth.currentUser?.uid ?: return
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

        val shopRepository = ShopInfoRepository(getApplication())
        val storage = FirestorageRepository()
        shopImageUri.value?.let { resourceUri ->
            storage.uploadImage(
                id = shop.id,
                imageUri = resourceUri,
                filePathScheme = StorageTypes.SHOP,
                context = appContext
            ) { imageResult ->
                if (imageResult.isSuccess) {
                    shopRepository.registerShop(shop, imageResult.getOrNull()) { registerResult ->
                        if (registerResult.isSuccess) {
                            registerResult.getOrNull()?.let {
                                shopId = id
                                auth.addPointShop()
                            }
                        } else {
                            Timber.e(registerResult.exceptionOrNull())
                        }
                        _isVisibleLoading.postValue(false)
                    }
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