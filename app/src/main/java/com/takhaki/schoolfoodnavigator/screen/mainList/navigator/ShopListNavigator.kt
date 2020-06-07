package com.takhaki.schoolfoodnavigator.screen.mainList.navigator

import com.takhaki.schoolfoodnavigator.screen.addShop.view.AddShopActivity
import com.takhaki.schoolfoodnavigator.screen.detail.view.DetailActivity
import com.takhaki.schoolfoodnavigator.screen.mainList.ShopListNavigatorAbstract
import com.takhaki.schoolfoodnavigator.screen.profile.view.ProfileActivity

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

    override fun toShopDetail(id: String, name: String) {
        weakActivity?.get()?.let { activity ->
            val intent = DetailActivity.makeIntent(activity, id, name)
            activity.startActivity(intent)
        }
    }

    override fun toProfilePage(id: String) {
        weakActivity?.get()?.let { activity ->
            val intent = ProfileActivity.makeIntent(activity, id)
            activity.startActivity(intent)
        }
    }

}