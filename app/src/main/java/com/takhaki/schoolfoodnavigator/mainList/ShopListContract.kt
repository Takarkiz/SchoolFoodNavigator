package com.takhaki.schoolfoodnavigator.mainList

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import com.takhaki.schoolfoodnavigator.BaseNavigator

/**
 * ショップリスト ViewModel-Base
 */
abstract class ShopListViewModelBase(
    application: Application
) : AndroidViewModel(application),
    LifecycleObserver,
    ShopListViewModelContract


/**
 * ショップリストViewModel-Contract
 */
interface ShopListViewModelContract {

    /**
     * ショップリスト一覧
     */
    val shopItemList: LiveData<List<ShopListItemModel>>

    /**
     * ショップリストのアクティビティ
     */
    fun activity(activity: AppCompatActivity)

    /**
     * ショップリストの読み込み
     */
    fun loadListShopItem()

    /**
     * タブタップ時のリスト切替
     */
    fun putTabNumber(index: Int)

}

abstract class ShopListNavigatorAbstract : BaseNavigator() {

    /**
     * ショップ追加画面への遷移
     */
    abstract fun toAddShopView()

    /**
     * ショップ詳細画面への遷移
     */
    abstract fun toShopDetail(id: String)
}