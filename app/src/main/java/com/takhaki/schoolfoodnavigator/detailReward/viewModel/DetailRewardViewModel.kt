package com.takhaki.schoolfoodnavigator.detailReward.viewModel

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.takhaki.schoolfoodnavigator.repository.UserAuth
import com.takhaki.schoolfoodnavigator.Utility.RewardUtil
import com.takhaki.schoolfoodnavigator.detailReward.DetailRewardViewModelBase

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

    // Private

    private val _userGrade =  MutableLiveData<RewardUtil.Grade>()

    private fun subscribeUserPoint() {

        val auth = UserAuth(getApplication())
        auth.currentUser?.uid?.let { uid ->
            auth.fetchUser(uid) {
                if (it.isSuccess) {
                    it.getOrNull()?.let { user ->
                        val point = user.score
                        _userGrade.postValue(RewardUtil.calculateUserRank(point))
                    }
                }
            }
        }
    }
}