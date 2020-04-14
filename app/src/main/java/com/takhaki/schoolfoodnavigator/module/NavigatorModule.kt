package com.takhaki.schoolfoodnavigator.module

import com.takhaki.schoolfoodnavigator.addShop.AddShopNavigator
import com.takhaki.schoolfoodnavigator.addShop.AddShopNavigatorAbstract
import com.takhaki.schoolfoodnavigator.assesment.AssessmentNavigator
import com.takhaki.schoolfoodnavigator.assesment.AssessmentNavigatorAbstract
import com.takhaki.schoolfoodnavigator.detail.DetailNavigator
import com.takhaki.schoolfoodnavigator.detail.DetailNavigatorAbstract
import com.takhaki.schoolfoodnavigator.mainList.ShopListNavigator
import com.takhaki.schoolfoodnavigator.mainList.ShopListNavigatorAbstract
import com.takhaki.schoolfoodnavigator.memberList.MemberListNavigatorAbstract
import com.takhaki.schoolfoodnavigator.memberList.navigator.MemberListNavigator
import com.takhaki.schoolfoodnavigator.profile.ProfileNavigator
import com.takhaki.schoolfoodnavigator.profile.ProfileNavigatorAbstract
import org.koin.dsl.module

val navigatorModule = module(override = true) {

    factory<ShopListNavigatorAbstract> { ShopListNavigator() }

    factory<DetailNavigatorAbstract> { DetailNavigator() }

    factory<AddShopNavigatorAbstract> { AddShopNavigator() }

    factory<AssessmentNavigatorAbstract> { AssessmentNavigator() }

    factory<ProfileNavigatorAbstract> { ProfileNavigator() }

    factory<MemberListNavigatorAbstract> { MemberListNavigator() }
}