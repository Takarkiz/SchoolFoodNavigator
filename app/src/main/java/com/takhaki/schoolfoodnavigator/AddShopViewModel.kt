package com.takhaki.schoolfoodnavigator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddShopViewModel : ViewModel() {

    private val _shopName = MutableLiveData<String>()
    val shopName: LiveData<String>
        get() = _shopName

    private val _genreTitle = MutableLiveData<String>()
    val genreTitle: LiveData<String>
        get() = _genreTitle


}