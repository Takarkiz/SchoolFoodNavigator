package com.takhaki.schoolfoodnavigator.addShop

import com.takhaki.schoolfoodnavigator.assesment.AssesmentActivity

/**
 * AddShop - Navigator
 */
class AddShopNavigator: AddShopNavigatorAbstract() {

    override fun toAssessment(id: String, name: String) {
        weakActivity?.get()?.let { activity ->
            val intent = AssesmentActivity.makeIntent(activity, id, name)
            activity.startActivity(intent)
        }
    }
}