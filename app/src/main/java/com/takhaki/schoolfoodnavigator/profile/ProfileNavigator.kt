package com.takhaki.schoolfoodnavigator.profile

import com.takhaki.schoolfoodnavigator.detailReward.view.DetailRewardActivity

class ProfileNavigator : ProfileNavigatorAbstract() {

    override fun toRewardDetail() {
        weakActivity?.get()?.let { activity ->
            val intent = DetailRewardActivity.makeIntent(activity)
            activity.startActivity(intent)
        }
    }
}