package com.takhaki.schoolfoodnavigator.detail

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
    DetailViewModelContract

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

abstract class DetailNavigatorAbstract: BaseNavigator() {

    abstract fun toAssessmentView(id: String, name: String)
}