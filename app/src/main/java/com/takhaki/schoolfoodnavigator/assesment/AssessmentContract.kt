package com.takhaki.schoolfoodnavigator.assesment

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.takhaki.schoolfoodnavigator.BaseNavigator
import com.takhaki.schoolfoodnavigator.Repository.AssessmentRespositoryContract

abstract class AssessmentViewModelBase(
    application: Application
)  : AndroidViewModel(application),
        LifecycleObserver,
        AssessmentViewModelContract

interface AssessmentViewModelContract {

    /**
     * 実行中Activityインスタンス
     */
    fun activity(activity: AppCompatActivity)

    /**
     * 美味しさ値
     */
    val goodValue: LiveData<Float>

    /**
     * 遠さ
     */
    val distanceValue: LiveData<Float>

    /**
     * 安さ
     */
    val cheepValue: LiveData<Float>

    /**
     * コメント
     */
    val commentText: MutableLiveData<String>

    /**
     * 良さの更新
     */
    fun onUpdateGood(rating: Float)

    /**
     * 距離の更新
     */
    fun onUpdateDistance(rating: Float)

    /**
     * 安さの更新
     */
    fun onUpdateCheep(rating: Float)

    /**
     * 評価のアップロード
     */
    fun uploadAssessment(finishUploadHandler: (Result<String>) -> Unit)

    /**
     * 評価のアップロード終了
     */
    fun finishUpload()
}

abstract class AssessmentNavigatorAbstract: BaseNavigator() {

    /**
     * お店一覧画面に戻る
     */
    abstract fun backToHome()
}