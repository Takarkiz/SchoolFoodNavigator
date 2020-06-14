package com.takhaki.schoolfoodnavigator.screen.register.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.takhaki.schoolfoodnavigator.repository.CompanyRepository
import com.takhaki.schoolfoodnavigator.repository.UserRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class CreateRoomViewModel(application: Application) : AndroidViewModel(application) {

    val titleText = MutableLiveData<String>().apply { value = "" }
    val hintText = MutableLiveData<String>().apply { value = "" }
    val contentEditText = MutableLiveData<String>().apply { value = "" }
    val isShowFinishButton = MediatorLiveData<Boolean>().apply { value = false }

    init {

        val inputTextObserver = Observer<String> {
            val inoutContent = contentEditText.value ?: ""
            isShowFinishButton.value = inoutContent.isNotBlank()
        }
        isShowFinishButton.addSource(contentEditText, inputTextObserver)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    fun putJoin(isJoin: Boolean) {
        if (isJoin) {
            titleText.value = "チームIDを入力してください"
            hintText.value = "チームID"
        } else {
            titleText.value = "チーム名を入力してください"
            hintText.value = "チーム名"
        }
    }

    // サインインを行う
    fun signInAuth() {
        val auth = UserRepository(getApplication())
        if (auth.currentUser != null) return
        auth.signInUser()
            .subscribeBy(
                onSuccess = {
                    Timber.d("登録完了！uid: $it")
                },
                onError = {
                    Timber.e(it)
                }
            ).addTo(disposable)
    }

    // チームを作成する
    fun createRoom(handler: (Int) -> Unit) {
        val repository = CompanyRepository(getApplication())
        val auth = UserRepository(getApplication())
        val uid = auth.currentUser?.uid ?: return

        contentEditText.value?.let { name ->
            repository.createCompanyRoom(name)
                .subscribeBy(
                    onSuccess = { teamId ->
                        repository.joinTeam(uid).subscribeBy(
                            onSuccess = {
                                handler(teamId)
                            }, onError = {
                                Timber.e(it)
                            }).addTo(disposable)
                    },
                    onError = {
                        Timber.d(it)
                    }).addTo(disposable)
        }
    }


    // チームID検索
    fun searchTeam(id: Int, handler: (Result<Boolean>) -> Unit) {
        val auth = UserRepository(getApplication())
        val uid = auth.currentUser?.uid?.let { it } ?: return
        val repository = CompanyRepository(getApplication())
        repository.searchCompany(id)
            .subscribeBy(onSuccess = { result ->
                if (result) {
                    repository.joinTeam(uid).subscribeBy(
                        onSuccess = {
                            handler(Result.success(true))
                        },
                        onError = { error ->
                            Timber.e(error)
                            handler(Result.failure(error))
                        }
                    )
                }
            },
                onError = {
                    handler(Result.failure(it))
                }).addTo(disposable)
    }

    private val disposable: CompositeDisposable = CompositeDisposable()

}