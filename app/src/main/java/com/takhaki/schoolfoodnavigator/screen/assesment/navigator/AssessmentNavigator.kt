package com.takhaki.schoolfoodnavigator.screen.assesment.navigator

import com.takhaki.schoolfoodnavigator.MainActivity
import com.takhaki.schoolfoodnavigator.screen.assesment.AssessmentNavigatorAbstract

class AssessmentNavigator : AssessmentNavigatorAbstract() {

    override fun backToHome() {
        weakActivity?.get()?.let { activity ->
            val intent = MainActivity.makeIntent(activity)
            activity.startActivity(intent)
        }
    }
}