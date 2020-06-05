package com.takhaki.schoolfoodnavigator.module

import com.takhaki.schoolfoodnavigator.screen.addShop.AddShopViewModel
import com.takhaki.schoolfoodnavigator.screen.assesment.AssessmentViewModel
import com.takhaki.schoolfoodnavigator.screen.detail.DetailViewModel
import com.takhaki.schoolfoodnavigator.screen.detailReward.viewModel.DetailRewardViewModel
import com.takhaki.schoolfoodnavigator.screen.mainList.ShopListViewModel
import com.takhaki.schoolfoodnavigator.screen.mainList.ShopListViewModelBase
import com.takhaki.schoolfoodnavigator.screen.memberList.view_model.MemberListViewModel
import com.takhaki.schoolfoodnavigator.screen.profile.ProfileViewModel
import com.takhaki.schoolfoodnavigator.screen.qrcode.view_model.QRCodeViewModel
import com.takhaki.schoolfoodnavigator.screen.register.CreateRoomViewModel
import com.takhaki.schoolfoodnavigator.screen.register.IconRegisterViewModel
import com.takhaki.schoolfoodnavigator.screen.register.NameRegisterViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module(override = true) {

    viewModel<ShopListViewModelBase> { ShopListViewModel(androidApplication(), get()) }

    viewModel { AddShopViewModel(androidApplication(), get()) }

    viewModel { (shopId: String) ->
        AssessmentViewModel(androidApplication(), shopId, get())
    }

    viewModel { (shopId: String) ->
        DetailViewModel(androidApplication(), shopId, get())
    }

    viewModel { (userId: String) ->
        ProfileViewModel(androidApplication(), userId, get())
    }

    viewModel { MemberListViewModel(androidApplication(), get()) }

    viewModel { QRCodeViewModel(androidApplication(), get()) }

    viewModel { DetailRewardViewModel(androidApplication()) }

    viewModel {
        CreateRoomViewModel(
            androidApplication()
        )
    }

    viewModel {
        IconRegisterViewModel(
            androidApplication()
        )
    }

    viewModel { NameRegisterViewModel() }
}