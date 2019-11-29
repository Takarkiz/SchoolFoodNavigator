package com.takhaki.schoolfoodnavigator.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.takhaki.schoolfoodnavigator.Model.CompanyData
import com.takhaki.schoolfoodnavigator.Model.UserEntity
import com.takhaki.schoolfoodnavigator.Repository.CompanyRepository
import com.takhaki.schoolfoodnavigator.Repository.UserAuth

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val _pageUserUid = MutableLiveData<String>()

    private val _userImageUrl = MutableLiveData<String>()
    val userImageUrl: LiveData<String>
        get() = _userImageUrl

    val userName = MutableLiveData<String>().apply { value = "ユーザー名" }
    val userPoint = MutableLiveData<Int>().apply { value = 0 }
    val userGradeTitle = MutableLiveData<String>().apply { value = "一般人" }
    val teamName = MutableLiveData<String>().apply { value = "" }

    fun updateUserProfile(uid: String) {
        _pageUserUid.value = uid
        getUser { user ->
            userName.value = user.name
            userPoint.value = user.navScore
            _userImageUrl.value = user.profImageUrl
            userGradeTitle.value = caliculateUserRank(user.navScore)
        }

        getUserTeamName()
    }

    private fun getUserTeamName() {
        val companyID = CompanyData.getCompanyId(getApplication()).toString()
        val repository = CompanyRepository(getApplication())
        repository.fetchCompanyName { result ->
            if (result.isSuccess) {
                result.getOrNull()?.let { name ->
                    teamName.value = "${name}(ID: ${companyID})"
                }
            }
        }
    }

    private fun getUser(handler: (UserEntity) -> Unit) {
        val auth = UserAuth(getApplication())
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
    private fun caliculateUserRank(point: Int): String {
        return when (point) {
            in 0..9 -> "一般人"
            in 10..19 -> "ちょっと詳しい人"
            in 20..39 -> "グルメな人"
            in 40..59 -> "通りの達人"
            in 60..79 -> "グルメ案内人"
            in 80..100 -> "グルメマスター"
            else -> "___"
        }
    }
}