package com.takhaki.schoolfoodnavigator.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.takhaki.schoolfoodnavigator.Model.UserEntity
import com.takhaki.schoolfoodnavigator.Repository.UserAuth

class ProfileViewModel : ViewModel() {

    private val _pageUserUid = MutableLiveData<String>()

    private val _userImageUrl = MutableLiveData<String>()
    val userImageUrl: LiveData<String>
        get() = _userImageUrl

    val userName = MutableLiveData<String>().apply { value = "ユーザー名" }
    val userPoint = MutableLiveData<Int>().apply { value = 0 }

    fun updateUserProfile(uid: String) {
        _pageUserUid.value = uid
        getUser { user ->
            userName.value = user.name
            userPoint.value = user.navScore
            _userImageUrl.value = user.profImageUrl
        }
    }

    private fun getUser(handler: (UserEntity) -> Unit) {
        val auth = UserAuth()
        _pageUserUid.value?.let { uid ->
            auth.fetchUser(uid) { result ->
                if (result.isSuccess) {
                    result.getOrNull()?.let { user ->
                        handler(user)
                    }
                }
            }
        }
    }

    // ユーザーの称号を計算するメソッド
    private fun caliculateUserRank() {

    }
}