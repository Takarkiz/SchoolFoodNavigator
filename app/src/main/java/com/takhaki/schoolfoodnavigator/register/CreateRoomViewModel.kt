package com.takhaki.schoolfoodnavigator.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.takhaki.schoolfoodnavigator.Model.Company
import com.takhaki.schoolfoodnavigator.Repository.CompanyRepository
import com.takhaki.schoolfoodnavigator.Repository.UserAuth

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
        val repository = CompanyRepository()
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
        val repository = CompanyRepository()
        repository.searchCompany(id) {
            handler(it)
        }
    }
}