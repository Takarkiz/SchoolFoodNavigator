package com.takhaki.schoolfoodnavigator.register

import android.net.Uri
import androidx.lifecycle.*

class IconRegisterViewModel : ViewModel() {

    private val _userName = MutableLiveData<String>()

    private val _iconImageUri = MutableLiveData<Uri>().apply { value = null }
    val iconImageUri: LiveData<Uri>
        get() = _iconImageUri

    val finishButtonTitle = MediatorLiveData<String>().apply { value = "アイコン設定をスキップ" }

    init {
        val iconUriObserver = Observer<Uri> {
            val uri = _iconImageUri.value
            uri?.let {
                finishButtonTitle.value = "ユーザー設定を終了する"
            }
        }
        finishButtonTitle.addSource(_iconImageUri, iconUriObserver)
    }

    fun putUserName(userName: String) {
        _userName.value = userName
    }

    fun putIconImage(imageUri: Uri) {
        _iconImageUri.value = imageUri
    }
}