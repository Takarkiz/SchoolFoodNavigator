package com.takhaki.schoolfoodnavigator.screen.detailReward.viewModel

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.takhaki.schoolfoodnavigator.Utility.RewardUtil
import com.takhaki.schoolfoodnavigator.repository.UserRepository
import com.takhaki.schoolfoodnavigator.screen.detailReward.DetailRewardViewModelBase
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DetailRewardViewModel(
    application: Application
) : DetailRewardViewModelBase(application) {

    override fun activity(activity: AppCompatActivity) {

    }

    override val userGrade: LiveData<RewardUtil.Grade>
        get() = _userGrade

    // LifecycleObserver

    @ExperimentalCoroutinesApi
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        subscribeUserPoint()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    // Private

    private val _userGrade = MutableLiveData<RewardUtil.Grade>()

    private val disposable = CompositeDisposable()

    @ExperimentalCoroutinesApi
    private fun subscribeUserPoint() {

        val auth = UserRepository(getApplication())
        viewModelScope.launch(Dispatchers.Main) {
            auth.currentUser?.uid?.let { uid ->
                auth.fetchUser(uid)
                    .collectLatest { user ->
                        val point = user.score
                        _userGrade.postValue(RewardUtil.calculateUserRank(point))
                    }
            }
        }
    }
}
