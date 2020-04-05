package com.takhaki.schoolfoodnavigator.module

import com.takhaki.schoolfoodnavigator.addShop.AddShopViewModel
import com.takhaki.schoolfoodnavigator.addShop.AddShopViewModelBase
import com.takhaki.schoolfoodnavigator.assesment.AssesmentViewModel
import com.takhaki.schoolfoodnavigator.detail.DetailViewModel
import com.takhaki.schoolfoodnavigator.mainList.ShopListViewModel
import com.takhaki.schoolfoodnavigator.mainList.ShopListViewModelBase
import com.takhaki.schoolfoodnavigator.profile.ProfileViewModel
import com.takhaki.schoolfoodnavigator.register.CreateRoomViewModel
import com.takhaki.schoolfoodnavigator.register.IconRegisterViewModel
import com.takhaki.schoolfoodnavigator.register.NameRegisterViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module(override = true) {

    viewModel<ShopListViewModelBase> { ShopListViewModel(androidApplication(), get()) }

    viewModel { AddShopViewModel(androidApplication(), get()) }

    viewModel { AssesmentViewModel(androidApplication()) }

    viewModel { DetailViewModel(androidApplication()) }

    viewModel { ProfileViewModel(androidApplication()) }

    viewModel { CreateRoomViewModel(androidApplication()) }

    viewModel { IconRegisterViewModel(androidApplication()) }

    viewModel { NameRegisterViewModel() }
}