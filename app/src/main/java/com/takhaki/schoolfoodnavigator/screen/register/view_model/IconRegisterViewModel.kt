package com.takhaki.schoolfoodnavigator.screen.register.view_model

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.*
import com.takhaki.schoolfoodnavigator.entity.CompanyData
import com.takhaki.schoolfoodnavigator.repository.FirestorageRepository
import com.takhaki.schoolfoodnavigator.repository.StorageTypes
import com.takhaki.schoolfoodnavigator.repository.UserRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class IconRegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val _userName = MutableLiveData<String>()
    private val context = getApplication<Application>().applicationContext

    private val _iconImageUri = MutableLiveData<Uri>().apply { value = null }
    val iconImageUri: LiveData<Uri> get() = _iconImageUri

    private val _isCompletedUserData = MutableLiveData<Boolean>().apply { value = false }
    val isCompletedUserData: LiveData<Boolean>
        get() = _isCompletedUserData

    private val finishButtonTitle = MediatorLiveData<String>().apply { value = "アイコン設定をスキップ" }

    private val disposable = CompositeDisposable()

    init {
        val iconUriObserver = Observer<Uri> {
            val uri = _iconImageUri.value
            uri?.let {
                finishButtonTitle.value = "ユーザー設定を終了する"
            }
        }
        finishButtonTitle.addSource(_iconImageUri, iconUriObserver)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    fun putUserName(userName: String) {
        _userName.postValue(userName)
    }

    fun putIconImage(imageUri: Uri) {
        _iconImageUri.postValue(imageUri)
    }

    fun createUser() {
        val auth = UserRepository(getApplication())
        val name = _userName.value ?: return
        val uid = auth.currentUser?.uid ?: return
        val storage = FirestorageRepository()
        _iconImageUri.value?.let { uri ->
            storage.uploadImage(uid, uri, StorageTypes.USER, getApplication()) { result ->
                if (result.isSuccess) {
                    val url = result.getOrNull()
                    registerUser(auth, name, url)
                } else {
                    Timber.e(result.exceptionOrNull())
                }
            }
        } ?: run {
            registerUser(auth, name, null)
        }
    }

    fun saveCompanyID(id: Int, context: Context) {
        CompanyData.saveCompanyId(id, context)
    }

    private fun registerUser(repository: UserRepository, name: String, url: String?) {
        repository.createUser(name, url)
            .subscribeBy(
                onSuccess = {
                    _isCompletedUserData.postValue(true)
                },
                onError = {
                    _isCompletedUserData.postValue(false)
                }
            ).addTo(disposable)
    }
}