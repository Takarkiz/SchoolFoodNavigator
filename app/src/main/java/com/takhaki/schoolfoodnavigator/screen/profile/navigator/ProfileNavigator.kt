package com.takhaki.schoolfoodnavigator.screen.profile.navigator

import com.takhaki.schoolfoodnavigator.screen.detailReward.view.DetailRewardActivity
import com.takhaki.schoolfoodnavigator.screen.memberList.view.MemberListActivity
import com.takhaki.schoolfoodnavigator.screen.profile.ProfileNavigatorAbstract
import com.takhaki.schoolfoodnavigator.screen.register.view.RegisterUserActivity

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