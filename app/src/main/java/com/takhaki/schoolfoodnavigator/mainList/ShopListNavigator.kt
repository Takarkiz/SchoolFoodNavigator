package com.takhaki.schoolfoodnavigator.mainList

import com.takhaki.schoolfoodnavigator.addShop.AddShopActivity
import com.takhaki.schoolfoodnavigator.profile.ProfileActivity

/**
 * ShopList - Navigator
 */
class ShopListNavigator: ShopListNavigatorAbstract() {

    override fun toAddShopView() {
        weakActivity?.get()?.let { activity ->
            val intent = AddShopActivity.makeIntent(activity)
            activity.startActivity(intent)
        }
    }

    override fun toShopDetail(id: String) {
        weakActivity?.get()?.let { activity ->

        }
    }

    override fun toProfilePage(id: String) {
        weakActivity?.get()?.let { activity ->
            val intent = ProfileActivity.makeIntent(activity, id)
            activity.startActivity(intent)
        }
    }

}