package com.takhaki.schoolfoodnavigator.assesment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AssesmentViewModel : ViewModel() {

    private val _shopId = MutableLiveData<String>()
    val shopId: LiveData<String>
        get() = _shopId

    private val _goodValue = MutableLiveData<Float>().apply { value = 0f }
    val goodValue: LiveData<Float>
        get() = _goodValue

    private val _distanceValue = MutableLiveData<Float>().apply { value = 0f }
    val distanceValue: LiveData<Float>
        get() = _distanceValue

    private val _cheepValue = MutableLiveData<Float>().apply { value = 0f }
    val cheepValue: LiveData<Float>
        get() = _cheepValue

    private val _isVisibleFinishButton = MutableLiveData<Boolean>()
    val isVisibleFinishButton: LiveData<Boolean>
        get() = _isVisibleFinishButton


    fun putShopId(shopId: String) {
        _shopId.value = shopId
    }

    fun onUpdateGood(rating: Float) {

    }

    fun onUpdateDistance(rating: Float) {

    }

    fun onUpdateCheep(rating: Float) {

    }

}