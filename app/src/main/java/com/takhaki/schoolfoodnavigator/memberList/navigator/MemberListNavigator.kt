package com.takhaki.schoolfoodnavigator.memberList.navigator

import com.takhaki.schoolfoodnavigator.memberList.MemberListNavigatorAbstract
import com.takhaki.schoolfoodnavigator.memberList.view.MemberListActivity
import com.takhaki.schoolfoodnavigator.profile.ProfileActivity

class MemberListNavigator : MemberListNavigatorAbstract() {

    override fun toMemberProfile(id: String) {
        weakActivity?.get()?.let { activity ->
            val intent = ProfileActivity.makeIntent(activity, id)
            activity.startActivity(intent)
        }
    }
}