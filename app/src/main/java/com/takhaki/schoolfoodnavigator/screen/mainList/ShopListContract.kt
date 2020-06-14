package com.takhaki.schoolfoodnavigator.screen.mainList

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import com.takhaki.schoolfoodnavigator.BaseNavigator
import com.takhaki.schoolfoodnavigator.screen.mainList.model.ShopListItemModel

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
    val shopItemLists: LiveData<List<List<ShopListItemModel>>>

    /**
     * 現在のユーザーのアイコンURL
     */
    val userIconUrl: LiveData<String>

    /**
     * ショップリストのアクティビティ
     */
    fun activity(activity: AppCompatActivity)

    /**
     * 追加アイコンをタップした時
     */
    fun didTapAddFabIcon()

    /**
     * 自分のプロフアイコンをタップした時
     */
    fun didTapOwnProfileIcon()

    /**
     * ショップをタップした時
     */
    fun didTapShopItem(id: String, name: String)

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
    abstract fun toShopDetail(id: String, name: String)

    /**
     * プロフィールページへの繊維
     */
    abstract fun toProfilePage(id: String)
}