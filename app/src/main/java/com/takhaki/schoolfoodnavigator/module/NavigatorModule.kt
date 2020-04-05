package com.takhaki.schoolfoodnavigator.module

import com.takhaki.schoolfoodnavigator.addShop.AddShopNavigator
import com.takhaki.schoolfoodnavigator.addShop.AddShopNavigatorAbstract
import com.takhaki.schoolfoodnavigator.mainList.ShopListNavigator
import com.takhaki.schoolfoodnavigator.mainList.ShopListNavigatorAbstract
import org.koin.dsl.module

val navigatorModule = module(override = true) {

    factory<ShopListNavigatorAbstract> { ShopListNavigator() }

    factory<AddShopNavigatorAbstract> { AddShopNavigator() }
}