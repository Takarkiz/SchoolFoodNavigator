package com.takhaki.schoolfoodnavigator.screen.register.view_model

import android.app.Application
import androidx.lifecycle.*
import com.takhaki.schoolfoodnavigator.repository.CompanyRepository
import com.takhaki.schoolfoodnavigator.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        viewModelScope.launch(Dispatchers.Default) {
            try {
                withContext(Dispatchers.Default) {
                    auth.signInUser()
                }
            } catch (e: Throwable) {
                Timber.e(e)
            }
        }
    }

    // チームを作成する
    fun createRoom(handler: (Int) -> Unit) {
        val repository = CompanyRepository(getApplication())
        val auth = UserRepository(getApplication())
        val uid = auth.currentUser?.uid ?: return

        viewModelScope.launch(Dispatchers.Default) {
            val teamId = withContext(Dispatchers.Default) {
                contentEditText.value?.let { name ->
                    repository.createCompanyRoom(name)
                }
            }

            try {
                withContext(Dispatchers.Default) {
                    repository.joinTeam(uid)
                }
                handler(teamId ?: 0)
            } catch (e: Throwable) {
                Timber.e(e)
            }
        }
    }


    // チームID検索
    suspend fun searchTeam(id: Int, handler: (Result<Boolean>) -> Unit) {
        val auth = UserRepository(getApplication())
        val uid = auth.currentUser?.uid ?: return
        val repository = CompanyRepository(getApplication())
        val isExist = withContext(Dispatchers.Default) {
            repository.searchCompany(id)
        }

        if (isExist) {
            viewModelScope.launch(Dispatchers.Default) {
                try {
                    withContext(Dispatchers.Default) {
                        repository.joinTeam(uid)
                    }
                    handler(Result.success(true))
                } catch (e: Throwable) {
                    Timber.e(e)
                    handler(Result.failure(e))
                }
            }
        } else {
            handler(Result.success(false))
        }
    }

}