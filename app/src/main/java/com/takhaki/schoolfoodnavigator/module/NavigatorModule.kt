package com.takhaki.schoolfoodnavigator.module

import com.takhaki.schoolfoodnavigator.screen.addShop.navigator.AddShopNavigator
import com.takhaki.schoolfoodnavigator.screen.addShop.AddShopNavigatorAbstract
import com.takhaki.schoolfoodnavigator.screen.assesment.navigator.AssessmentNavigator
import com.takhaki.schoolfoodnavigator.screen.assesment.AssessmentNavigatorAbstract
import com.takhaki.schoolfoodnavigator.screen.detail.navigator.DetailNavigator
import com.takhaki.schoolfoodnavigator.screen.detail.DetailNavigatorAbstract
import com.takhaki.schoolfoodnavigator.screen.mainList.navigator.ShopListNavigator
import com.takhaki.schoolfoodnavigator.screen.mainList.ShopListNavigatorAbstract
import com.takhaki.schoolfoodnavigator.screen.memberList.MemberListNavigatorAbstract
import com.takhaki.schoolfoodnavigator.screen.memberList.navigator.MemberListNavigator
import com.takhaki.schoolfoodnavigator.screen.profile.navigator.ProfileNavigator
import com.takhaki.schoolfoodnavigator.screen.profile.ProfileNavigatorAbstract
import com.takhaki.schoolfoodnavigator.screen.qrcode.QRCodeNavigatorAbstract
import com.takhaki.schoolfoodnavigator.screen.qrcode.navigator.QRCodeNavigator
import org.koin.dsl.module

val navigatorModule = module(override = true) {

    factory<ShopListNavigatorAbstract> { ShopListNavigator() }

    factory<DetailNavigatorAbstract> { DetailNavigator() }

    factory<AddShopNavigatorAbstract> { AddShopNavigator() }

    factory<AssessmentNavigatorAbstract> { AssessmentNavigator() }

    factory<ProfileNavigatorAbstract> { ProfileNavigator() }

    factory<MemberListNavigatorAbstract> { MemberListNavigator() }

    factory<QRCodeNavigatorAbstract> { QRCodeNavigator() }
}