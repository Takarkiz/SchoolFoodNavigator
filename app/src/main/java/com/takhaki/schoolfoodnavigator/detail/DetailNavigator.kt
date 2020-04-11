package com.takhaki.schoolfoodnavigator.detail

import com.takhaki.schoolfoodnavigator.assesment.AssessmentActivity

class DetailNavigator : DetailNavigatorAbstract() {

    override fun toAssessmentView(id: String, name: String) {
        weakActivity?.get()?.let { activity ->
            val intent = AssessmentActivity.makeIntent(activity, id, name)
            activity.startActivity(intent)
        }
    }

}