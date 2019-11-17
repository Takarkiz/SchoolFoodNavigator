package com.takhaki.schoolfoodnavigator.register

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

class NameRegisterViewModel : ViewModel() {

    val nameTextView = MutableLiveData<String>().apply{ value = ""}

    val isVisibleUserFinishButton = MediatorLiveData<Boolean>().apply { value = false }


    init {
        val nameTextViewObserver = Observer<String> {
            val name = nameTextView.value ?: ""
            isVisibleUserFinishButton.value = name.isNotBlank()
        }
        isVisibleUserFinishButton.addSource(nameTextView, nameTextViewObserver)
    }
}