package com.takhaki.schoolfoodnavigator.addShop

import android.app.Application
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.takhaki.schoolfoodnavigator.BaseNavigator

/**
 * ショップの新規追加 ViewModel-Base
 */
abstract class AddShopViewModelBase(
    application: Application
) : AndroidViewModel(application),
    LifecycleObserver,
    AddShopViewModelContract

/**
 * ショップの新規追加 ViewModel-Contract
 */
interface AddShopViewModelContract {

    /**
     * ショップの名前
     */
    val shopName: MutableLiveData<String>

    /**
     * ショップのジャンル
     */
    val genreTitle: MutableLiveData<String>

    /**
     * 終了ボタンの表示の有無
     */
    val isVisibleFinishButton: MediatorLiveData<Boolean>

    /**
     * ローディング表示の有無
     */
    val isVisibleLoading: LiveData<Boolean>

    /**
     * お店の画像Uri
     */
    val shopImageUri: MediatorLiveData<Uri>

    /**
     * お店追加のアクティビティ
     */
    fun activity(activity: AppCompatActivity)

    /**
     * お店情報のアップロード
     */
    fun uploadShopInfo()

    /**
     * 評価画面への遷移
     */
    fun willMoveToAssessment()

    /**
     * ショップ一覧に戻る
     */
    fun backToShopList()

    /**
     * 仮登録の写真の削除
     */
    fun deletePhoto()
}

/**
 * AddShop-Navigator
 */
abstract class AddShopNavigatorAbstract : BaseNavigator() {

    /**
     * 評価画面への遷移
     */
    abstract fun toAssessment(id: String, name: String)

}