package com.takhaki.schoolfoodnavigator.screen.memberList.navigator

import com.takhaki.schoolfoodnavigator.screen.memberList.MemberListNavigatorAbstract
import com.takhaki.schoolfoodnavigator.screen.profile.ProfileActivity
import com.takhaki.schoolfoodnavigator.screen.qrcode.view.QRCodeActivity

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