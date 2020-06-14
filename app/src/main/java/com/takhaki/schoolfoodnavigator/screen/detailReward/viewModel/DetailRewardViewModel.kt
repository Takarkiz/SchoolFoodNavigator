package com.takhaki.schoolfoodnavigator.screen.detailReward.viewModel

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.takhaki.schoolfoodnavigator.Utility.RewardUtil
import com.takhaki.schoolfoodnavigator.repository.UserRepository
import com.takhaki.schoolfoodnavigator.screen.detailReward.DetailRewardViewModelBase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class DetailRewardViewModel(
    application: Application
) : DetailRewardViewModelBase(application) {

    override fun activity(activity: AppCompatActivity) {

    }

    override val userGrade: LiveData<RewardUtil.Grade>
        get() = _userGrade

    // LifecycleObserver

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

    private fun subscribeUserPoint() {

        val auth = UserRepository(getApplication())
        auth.currentUser?.uid?.let { uid ->
            auth.fetchUser(uid)
                .subscribeBy(
                    onNext = { user ->
                        val point = user.score
                        _userGrade.postValue(RewardUtil.calculateUserRank(point))

                    }, onError = {
                        Timber.e(it)
                    }
                ).addTo(disposable)
        }
    }
}