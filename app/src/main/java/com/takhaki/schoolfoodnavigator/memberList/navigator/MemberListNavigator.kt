package com.takhaki.schoolfoodnavigator.memberList.navigator

import com.takhaki.schoolfoodnavigator.memberList.MemberListNavigatorAbstract
import com.takhaki.schoolfoodnavigator.memberList.view.MemberListActivity
import com.takhaki.schoolfoodnavigator.profile.ProfileActivity
import com.takhaki.schoolfoodnavigator.qrcode.view.QRCodeActivity

class MemberListNavigator : MemberListNavigatorAbstract() {

    override fun toMemberProfile(id: String) {
        weakActivity?.get()?.let { activity ->
            val intent = ProfileActivity.makeIntent(activity, id)
            activity.startActivity(intent)
        }
    }

    override fun toAddMemberView() {
        weakActivity?.get()?.let { activity ->
            val intent = QRCodeActivity.makeIntent(activity)
            activity.startActivity(intent)
        }
    }
}