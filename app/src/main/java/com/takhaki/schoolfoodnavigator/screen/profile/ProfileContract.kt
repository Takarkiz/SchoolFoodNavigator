package com.takhaki.schoolfoodnavigator.screen.profile

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import com.takhaki.schoolfoodnavigator.BaseNavigator
import com.takhaki.schoolfoodnavigator.entity.UserEntity

/**
 * プロフィール ViewModel-Base
 */
abstract class ProfileViewModelBase(
    application: Application
) : AndroidViewModel(application),
    LifecycleObserver,
    ProfileViewModelContract

/**
 * プロフィール ViewModel-Contract
 */
interface ProfileViewModelContract {

    /**
     * 実行中のアクティビティ
     */
    fun activity(activity: AppCompatActivity)

    /**
     * 所属組織名
     */
    val teamName: LiveData<String>

    /**
     * ユーザー情報
     */
    val user: LiveData<UserEntity>

    /**
     * 称号の詳細ページに遷移
     */
    fun didTapShowRewardDetail()

    /**
     * 組織名タップ時
     */
    fun didTapCompanyText()

    /**
     * ユーザーを削除
     */
    fun didTapDeleteUser()

}

/**
 * プロフィール Navigator
 */
abstract class ProfileNavigatorAbstract : BaseNavigator() {

    /**
     * 称号の詳細画面を表示
     */
    abstract fun toRewardDetail()

    /**
     * メンバー一覧へ遷移
     */
    abstract fun toMemberList()

    /**
     * 最初の画面に戻る
     */
    abstract fun toFirstView()
}