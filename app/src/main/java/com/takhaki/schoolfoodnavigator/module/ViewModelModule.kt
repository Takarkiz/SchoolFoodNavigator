package com.takhaki.schoolfoodnavigator.module

import com.takhaki.schoolfoodnavigator.screen.addShop.view_model.AddShopViewModel
import com.takhaki.schoolfoodnavigator.screen.assesment.view_model.AssessmentViewModel
import com.takhaki.schoolfoodnavigator.screen.detail.view_model.DetailViewModel
import com.takhaki.schoolfoodnavigator.screen.detailReward.viewModel.DetailRewardViewModel
import com.takhaki.schoolfoodnavigator.screen.mainList.view_model.ShopListViewModel
import com.takhaki.schoolfoodnavigator.screen.mainList.ShopListViewModelBase
import com.takhaki.schoolfoodnavigator.screen.memberList.view_model.MemberListViewModel
import com.takhaki.schoolfoodnavigator.screen.profile.view_model.ProfileViewModel
import com.takhaki.schoolfoodnavigator.screen.qrcode.view_model.QRCodeViewModel
import com.takhaki.schoolfoodnavigator.screen.register.view_model.CreateRoomViewModel
import com.takhaki.schoolfoodnavigator.screen.register.view_model.IconRegisterViewModel
import com.takhaki.schoolfoodnavigator.screen.register.view_model.NameRegisterViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module(override = true) {

    viewModel<ShopListViewModelBase> {
        ShopListViewModel(
            androidApplication(),
            get()
        )
    }

    viewModel {
        AddShopViewModel(
            androidApplication(),
            get()
        )
    }

    viewModel { (shopId: String) ->
        AssessmentViewModel(
            androidApplication(),
            shopId,
            get()
        )
    }

    viewModel { (shopId: String) ->
        DetailViewModel(
            androidApplication(),
            shopId,
            get()
        )
    }

    viewModel { (userId: String) ->
        ProfileViewModel(
            androidApplication(),
            userId,
            get()
        )
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