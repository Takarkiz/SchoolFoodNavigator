package com.takhaki.schoolfoodnavigator.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import com.takhaki.schoolfoodnavigator.BaseNavigator

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
     * プロフィール画像のURL
     */
    val userImageUrl: LiveData<String>

    /**
     * ユーザー名
     */
    val userName: LiveData<String>

    /**
     * ユーザーの文字しているポイント
     */
    val userPoint: LiveData<Int>

    /**
     * ユーザーの称号名
     */
    val userGradeTitle: LiveData<String>

    /**
     * 所属組織名
     */
    val teamName: LiveData<String>

}

/**
 * プロフィール Navigator
 */
abstract class ProfileNavigatorAbstract : BaseNavigator()