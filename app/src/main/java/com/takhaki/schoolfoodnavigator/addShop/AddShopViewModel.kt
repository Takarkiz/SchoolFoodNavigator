package com.takhaki.schoolfoodnavigator.addShop

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.takhaki.schoolfoodnavigator.Model.ShopEntity
import com.takhaki.schoolfoodnavigator.Repository.ShopInfoRepository
import com.takhaki.schoolfoodnavigator.Repository.UserAuth
import java.util.*

class AddShopViewModel(application: Application) : AndroidViewModel(application) {

    val shopName = MutableLiveData<String>().apply { value = "" }
    val genreTitle = MutableLiveData<String>().apply { value = "" }
    private val _shopId = MutableLiveData<String>()
    val shopId: LiveData<String>
        get() = _shopId

    var isVisibleFinishButton = MediatorLiveData<Boolean>().apply { value = false }
    val isVisibleDeleteButton = MediatorLiveData<Boolean>().apply { value = false }
    val shopImageUri = MediatorLiveData<Uri>().apply { value = null }

    private var _willIntentAssessment = MutableLiveData<Boolean>().apply { value = false }
    val willIntentAssessment: LiveData<Boolean>
        get() = _willIntentAssessment

    val isVisibleLoading = MutableLiveData<Boolean>().apply { value = false }

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

        val auth = UserAuth(getApplication())
        val userID = auth.currentUser?.uid ?: return
        isVisibleLoading.value = true
        val shopId = UUID.randomUUID().toString()

        val shop = ShopEntity(
            id = shopId,
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
                    _shopId.value = id
                    _willIntentAssessment.value = true
                    auth.addPointShop()
                }

            } else {
                Log.e("Firebase", "失敗", result.exceptionOrNull())
            }
            isVisibleLoading.value = false
        }
    }

    fun toIntentAssessment(shouldGoAssessments: Boolean) {
        _willIntentAssessment.value = shouldGoAssessments
    }

    fun deletePhoto() {
        isVisibleDeleteButton.value = false
        shopImageUri.value = null
    }

    private val appContext: Context get() = getApplication()

}