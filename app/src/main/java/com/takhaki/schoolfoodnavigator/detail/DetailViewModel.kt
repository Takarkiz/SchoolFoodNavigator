package com.takhaki.schoolfoodnavigator.detail

import androidx.lifecycle.ViewModel

class DetailViewModel : ViewModel() {

    private var shopId: String = ""

    fun putShopId(id: String) {
        shopId = id
    }


}