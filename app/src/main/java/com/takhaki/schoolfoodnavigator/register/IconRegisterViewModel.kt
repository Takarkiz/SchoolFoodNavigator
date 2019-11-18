package com.takhaki.schoolfoodnavigator.register

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import com.takhaki.schoolfoodnavigator.Repository.UserAuth

class IconRegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val _userName = MutableLiveData<String>()
    private val context = getApplication<Application>().applicationContext

    private val _iconImageUri = MutableLiveData<Uri>().apply { value = null }
    val iconImageUri: LiveData<Uri>
        get() = _iconImageUri

    private val _isCompletedUserData = MutableLiveData<Boolean>().apply { value = false }
    val isCompletedUserData: LiveData<Boolean>
        get() = _isCompletedUserData

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

    fun createUser() {
        val auth = UserAuth()
        if (auth.currentUser != null) {
            return
        }
        val name = _userName.value ?: return
        val iconUri = _iconImageUri.value

        auth.createUser(name, iconUri, context) { result ->
            if (result.isSuccess) {
                _isCompletedUserData.value = true
            }
            // TODO: - エラーハンドリングする
        }
    }
}