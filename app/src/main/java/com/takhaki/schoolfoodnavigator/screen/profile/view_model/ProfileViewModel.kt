package com.takhaki.schoolfoodnavigator.screen.profile.view_model

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.takhaki.schoolfoodnavigator.Utility.RewardUtil.Companion.calculateUserRank
import com.takhaki.schoolfoodnavigator.entity.CompanyData
import com.takhaki.schoolfoodnavigator.entity.UserEntity
import com.takhaki.schoolfoodnavigator.repository.CompanyRepository
import com.takhaki.schoolfoodnavigator.repository.UserRepository
import com.takhaki.schoolfoodnavigator.screen.profile.ProfileNavigatorAbstract
import com.takhaki.schoolfoodnavigator.screen.profile.ProfileViewModelBase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
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
        CompanyData.deleteCompanyId(getApplication())
        navigator.toFirstView()
    }

    // LifecycleObserver

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        updateUserProfile()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    // Private
    private val _teamName = MutableLiveData<String>().apply { value = "" }
    private val _user = MutableLiveData<UserEntity>()

    private val disposable = CompositeDisposable()

    private fun updateUserProfile() {
        getUser()

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

    private fun getUser() {
        val auth = UserRepository(getApplication())
        auth.fetchUser(userId)
            .subscribeBy(
                onSuccess = {
                    _user.postValue(it)
                },
                onError = {
                    Timber.e(it)
                }
            ).addTo(disposable)
    }
}