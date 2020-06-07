package com.takhaki.schoolfoodnavigator.screen.memberList

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import com.takhaki.schoolfoodnavigator.BaseNavigator
import com.takhaki.schoolfoodnavigator.model.UserEntity
import com.takhaki.schoolfoodnavigator.screen.memberList.view.MemberListAdapter


abstract class MemberListViewModelBase(
    application: Application
) : AndroidViewModel(application),
    LifecycleObserver,
    MemberListViewModelContract,
    MemberListAdapter.MemberListClickListener

interface MemberListViewModelContract {

    /**
     * メンバー一覧
     */
    val memberList: LiveData<List<UserEntity>>

    /**
     * メンバーリストのアクティビティ
     */
    fun activity(activity: AppCompatActivity)


    /**
     * メンバーの追加ボタンをタップした時
     */
    fun didTapAddMember()

}

abstract class MemberListNavigatorAbstract : BaseNavigator() {

    /**
     * メンバーのプロフィールページ
     */
    abstract fun toMemberProfile(id: String)

    /**
     * メンバーの追加ページ
     */
    abstract fun toAddMemberView()
}