package com.takhaki.schoolfoodnavigator.screen.detail

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import com.takhaki.schoolfoodnavigator.BaseNavigator

abstract class DetailViewModelBase(
    application: Application
) : AndroidViewModel(application),
    LifecycleObserver,
    DetailViewModelContract,
    DetailAdapter.UserIconClickListener

interface DetailViewModelContract {

    /**
     * 実行中アクティビティ
     */
    fun activity(activity: AppCompatActivity)

    /**
     * ショップの詳細モデル
     */
    val shopDetail: LiveData<AboutShopDetailModel>

    /**
     * ショップの評価リスト
     */
    val scoreList: LiveData<List<CommentDetailModel>>

    /**
     * ユーザーがすでに評価しているかどうか
     */
    val hasCurrentUserComment: LiveData<Boolean>

    /**
     * 新規評価ボタンをタップした時
     */
    fun didTapAddFab()
}

abstract class DetailNavigatorAbstract : BaseNavigator() {

    /**
     * 評価画面への遷移
     */
    abstract fun toAssessmentView(id: String, name: String)

    /**
     * ユーザー詳細画面へ遷移
     */
    abstract fun toUserProfile(id: String)
}