package com.takhaki.schoolfoodnavigator.screen.profile

import com.takhaki.schoolfoodnavigator.screen.detailReward.view.DetailRewardActivity
import com.takhaki.schoolfoodnavigator.screen.memberList.view.MemberListActivity
import com.takhaki.schoolfoodnavigator.screen.register.RegisterUserActivity

class ProfileNavigator : ProfileNavigatorAbstract() {

    override fun toRewardDetail() {
        weakActivity?.get()?.let { activity ->
            val intent = DetailRewardActivity.makeIntent(activity)
            activity.startActivity(intent)
        }
    }

    override fun toMemberList() {
        weakActivity?.get()?.let { activity ->
            val intent = MemberListActivity.makeIntent(activity)
            activity.startActivity(intent)
        }
    }

    override fun toFirstView() {
        weakActivity?.get()?.let { activity ->
            val intent = RegisterUserActivity.makeIntent(activity)
            activity.startActivity(intent)
        }
    }
}