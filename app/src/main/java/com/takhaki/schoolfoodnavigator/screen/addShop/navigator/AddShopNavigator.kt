package com.takhaki.schoolfoodnavigator.screen.addShop.navigator

import com.takhaki.schoolfoodnavigator.MainActivity
import com.takhaki.schoolfoodnavigator.screen.addShop.AddShopNavigatorAbstract
import com.takhaki.schoolfoodnavigator.screen.assesment.activity.AssessmentActivity

/**
 * AddShop - Navigator
 */
class AddShopNavigator: AddShopNavigatorAbstract() {

    override fun toAssessment(id: String, name: String) {
        weakActivity?.get()?.let { activity ->
            val intent = AssessmentActivity.makeIntent(activity, id, name)
            activity.startActivity(intent)
        }
    }

    override fun backToHome() {
        weakActivity?.get()?.let { activity ->
            val intent = MainActivity.makeIntent(activity)
            activity.startActivity(intent)
        }
    }
}