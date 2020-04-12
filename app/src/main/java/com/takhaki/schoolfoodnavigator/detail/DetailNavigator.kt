package com.takhaki.schoolfoodnavigator.detail

import com.takhaki.schoolfoodnavigator.assesment.AssessmentActivity
import com.takhaki.schoolfoodnavigator.profile.ProfileActivity

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