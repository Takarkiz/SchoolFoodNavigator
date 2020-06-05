package com.takhaki.schoolfoodnavigator.screen.detailReward

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import com.takhaki.schoolfoodnavigator.Utility.RewardUtil

abstract class DetailRewardViewModelBase(
    application: Application
) : AndroidViewModel(application),
    LifecycleObserver,
    DetailRewardViewModelContract

/**
 * 称号詳細 ViewModel-Contract
 */
interface DetailRewardViewModelContract {

    /**
     * 実行中アクティビティ
     */
    fun activity(activity: AppCompatActivity)

    /**
     * ユーザー自身の称号
     */
    val userGrade: LiveData<RewardUtil.Grade>
}