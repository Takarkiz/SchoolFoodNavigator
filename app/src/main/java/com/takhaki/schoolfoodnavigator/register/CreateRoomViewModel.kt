package com.takhaki.schoolfoodnavigator.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.takhaki.schoolfoodnavigator.repository.CompanyRepository
import com.takhaki.schoolfoodnavigator.repository.UserAuth

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
        val auth = UserAuth(getApplication())
        if (auth.currentUser != null) return
        auth.signInUser { result ->
            if (result.isSuccess) {
                result.getOrNull()?.let { uid ->

                }
            }
        }
    }

    // チームを作成する
    fun createRoom(handler: (Int) -> Unit) {
        val repository = CompanyRepository(getApplication())
        val auth = UserAuth(getApplication())
        val uid = auth.currentUser?.uid?.let { it } ?: return

        contentEditText.value?.let { name ->
            repository.createCompany(name) { result ->
                if (result.isSuccess) {
                    result.getOrNull()?.let { teamId ->
                        repository.joinMember(uid, teamId)
                        handler(teamId)
                    }
                }
            }
        }
    }

    // チームID検索
    fun searchTeam(id: Int, handler: (Result<Boolean>) -> Unit) {
        val auth = UserAuth(getApplication())
        val uid = auth.currentUser?.uid?.let { it } ?: return
        val repository = CompanyRepository(getApplication())
        repository.searchCompany(id) {
            if (it.isSuccess) {
                it.getOrNull()?.let { result ->
                    if (result) {
                        repository.joinMember(uid, id)
                    }
                }

            }
            handler(it)
        }
    }
}