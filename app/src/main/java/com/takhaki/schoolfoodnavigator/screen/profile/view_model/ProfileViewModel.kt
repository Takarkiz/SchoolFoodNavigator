package com.takhaki.schoolfoodnavigator.screen.profile.view_model

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.takhaki.schoolfoodnavigator.entity.UserEntity
import com.takhaki.schoolfoodnavigator.repository.CompanyRepository
import com.takhaki.schoolfoodnavigator.repository.UserRepository
import com.takhaki.schoolfoodnavigator.screen.profile.ProfileNavigatorAbstract
import com.takhaki.schoolfoodnavigator.screen.profile.ProfileViewModelBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.ref.WeakReference

class ProfileViewModel(
    application: Application,
    private val userId: String,
    private val navigator: ProfileNavigatorAbstract
) : ProfileViewModelBase(application) {

    override fun activity(activity: AppCompatActivity) {
        navigator.weakActivity = WeakReference(activity)
    }

    override val teamName: LiveData<String> get() = _teamName
    override val user: LiveData<UserEntity> get() = _user

    override fun didTapShowRewardDetail() {
        navigator.toRewardDetail()
    }

    override fun didTapCompanyText() {
        navigator.toMemberList()
    }

    override fun didTapDeleteUser() {
        val companyRepository = CompanyRepository(getApplication())
        companyRepository.deleteCompanyIdCache()
        navigator.toFirstView()
    }

    // LifecycleObserver

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        updateUserProfile()
    }

    // Private
    private val _teamName = MutableLiveData<String>().apply { value = "" }
    private val _user = MutableLiveData<UserEntity>()

    private fun updateUserProfile() {
        getUser()
        getUserTeamName()

    }

    private fun getUserTeamName() {
        viewModelScope.launch(Dispatchers.Default) {
            val repository = CompanyRepository(getApplication())
            val companyID = repository.companyId.toString()
            repository.company.collect {
                if (it.isSuccess) {
                    it.getOrNull()?.let { company ->
                        _teamName.postValue("${company.name}(ID: ${companyID})")
                    }
                } else {
                    Timber.e(it.exceptionOrNull())
                }
            }
        }
    }

    private fun getUser() {
        viewModelScope.launch(Dispatchers.Default) {
            val auth = UserRepository(getApplication())
            auth.fetchUser(userId)
                .collect {
                    _user.postValue(it)
                }
        }
    }
}