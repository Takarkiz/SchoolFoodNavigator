package com.takhaki.schoolfoodnavigator.screen.profile

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.takhaki.schoolfoodnavigator.model.CompanyData
import com.takhaki.schoolfoodnavigator.model.UserEntity
import com.takhaki.schoolfoodnavigator.repository.CompanyRepository
import com.takhaki.schoolfoodnavigator.repository.UserAuth
import com.takhaki.schoolfoodnavigator.Utility.RewardUtil.Companion.calculateUserRank
import java.lang.ref.WeakReference

class ProfileViewModel(
    application: Application,
    private val userId: String,
    private val navigator: ProfileNavigatorAbstract
) : ProfileViewModelBase(application) {

    override fun activity(activity: AppCompatActivity) {
        navigator.weakActivity = WeakReference(activity)
    }

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

    override fun didTapShowRewardDetail() {
        navigator.toRewardDetail()
    }

    override fun didTapCompanyText() {
        navigator.toMemberList()
    }

    override fun didTapDeleteUser() {
        CompanyData.deleteCompanyId(getApplication())
        navigator.toFirstView()
    }

    // LifecycleObserver

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        updateUserProfile()
    }

    // Private
    private val _userImageUrl = MutableLiveData<String>()
    private val _userName = MutableLiveData<String>().apply { value = "ユーザー名" }
    private val _userPoint = MutableLiveData<Int>().apply { value = 0 }
    private val _userGradeTitle = MutableLiveData<String>().apply { value = "--" }
    private val _teamName = MutableLiveData<String>().apply { value = "" }

    private fun updateUserProfile() {
        getUser { user ->
            _userName.value = user.name
            _userPoint.value = user.score
            _userImageUrl.value = user.iconUrl
            _userGradeTitle.value = calculateUserRank(user.score).text
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
}