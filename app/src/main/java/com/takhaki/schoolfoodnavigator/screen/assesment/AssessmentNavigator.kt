package com.takhaki.schoolfoodnavigator.screen.assesment

import com.takhaki.schoolfoodnavigator.MainActivity

class AssessmentNavigator : AssessmentNavigatorAbstract() {

    override fun backToHome() {
        weakActivity?.get()?.let { activity ->
            val intent = MainActivity.makeIntent(activity)
            activity.startActivity(intent)
        }
    }
}