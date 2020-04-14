package com.takhaki.schoolfoodnavigator.profile

import com.takhaki.schoolfoodnavigator.detailReward.view.DetailRewardActivity
import com.takhaki.schoolfoodnavigator.memberList.view.MemberListActivity

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
}