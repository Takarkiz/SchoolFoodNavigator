package com.takhaki.schoolfoodnavigator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AssesmentViewModelFactory(private val shopId: String) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AssesmentViewModel::class.java)) {
            return AssesmentViewModel(shopId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}