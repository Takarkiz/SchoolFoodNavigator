package com.takhaki.schoolfoodnavigator.profile

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.takhaki.schoolfoodnavigator.Model.CompanyData
import com.takhaki.schoolfoodnavigator.Model.UserEntity
import com.takhaki.schoolfoodnavigator.Repository.CompanyRepository
import com.takhaki.schoolfoodnavigator.Repository.UserAuth

class ProfileViewModel(
    application: Application,
    private val userId: String,
    private val navigator: ProfileNavigatorAbstract
) : ProfileViewModelBase(application) {

    override val userImageUrl: LiveData<String>
        get() = _userImageUrl

    override val userName: LiveData<String>
        get() = _userName
    override val userPoint: LiveData<Int>
        get() = _userPoint
    override val userGradeTitle: LiveData<String>
        get() = _userGradeTitle
    override val teamName: LiveData<String>
        get() = _teamName


    // LifecycleObserver

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        updateUserProfile()
    }

    // Private
    private val _userImageUrl = MutableLiveData<String>()
    private val _userName = MutableLiveData<String>().apply { value = "ユーザー名" }
    private val _userPoint = MutableLiveData<Int>().apply { value = 0 }
    private val _userGradeTitle = MutableLiveData<String>().apply { value = "一般人" }
    private val _teamName = MutableLiveData<String>().apply { value = "" }

    private fun updateUserProfile() {
        getUser { user ->
            _userName.value = user.name
            _userPoint.value = user.navScore
            _userImageUrl.value = user.profImageUrl
            _userGradeTitle.value = caliculateUserRank(user.navScore)
        }

        getUserTeamName()
    }

    private fun getUserTeamName() {
        val companyID = CompanyData.getCompanyId(getApplication()).toString()
        val repository = CompanyRepository(getApplication())
        repository.fetchCompanyName { result ->
            if (result.isSuccess) {
                result.getOrNull()?.let { name ->
                    _teamName.value = "${name}(ID: ${companyID})"
                }
            }
        }
    }

    private fun getUser(handler: (UserEntity) -> Unit) {
        val auth = UserAuth(getApplication())
        auth.fetchUser(userId) { result ->
            if (result.isSuccess) {
                result.getOrNull()?.let { user ->
                    handler(user)
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