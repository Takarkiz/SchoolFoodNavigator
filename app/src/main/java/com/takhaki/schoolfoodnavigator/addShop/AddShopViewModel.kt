package com.takhaki.schoolfoodnavigator.addShop

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.takhaki.schoolfoodnavigator.Model.ShopEntity
import com.takhaki.schoolfoodnavigator.Repository.ShopInfoRepository
import com.takhaki.schoolfoodnavigator.Repository.UserAuth
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

    override val isVisibleLoading: MutableLiveData<Boolean>
        get() = _isVisibleLoading

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

        val shopInfoRepository = ShopInfoRepository(getApplication())
        shopInfoRepository.registerShop(shop, shopImageUri.value, appContext) { result ->
            if (result.isSuccess) {
                result.getOrNull()?.let { id ->
                    shopId = id
                    auth.addPointShop()
                }
            } else {
                Log.e("Firebase", "失敗", result.exceptionOrNull())
            }
            _isVisibleLoading.value = false
        }
    }

    override fun willMoveToAssessment() {
        navigator.toAssessment(shopId, shopName.value?: "")
    }

    override fun backToShopList() {
        navigator.finish()
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