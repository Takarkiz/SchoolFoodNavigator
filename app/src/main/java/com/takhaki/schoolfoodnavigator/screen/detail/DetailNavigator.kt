package com.takhaki.schoolfoodnavigator.screen.detail

import com.takhaki.schoolfoodnavigator.screen.assesment.AssessmentActivity
import com.takhaki.schoolfoodnavigator.screen.profile.ProfileActivity

class DetailNavigator : DetailNavigatorAbstract() {

    override fun toAssessmentView(id: String, name: String) {
        weakActivity?.get()?.let { activity ->
            val intent = AssessmentActivity.makeIntent(activity, id, name)
            activity.startActivity(intent)
        }
    }

    override fun toUserProfile(id: String) {
        weakActivity?.get()?.let { activity ->
            val intent = ProfileActivity.makeIntent(activity, id)
            activity.startActivity(intent)
        }
    }

}