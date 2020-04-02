package com.takhaki.schoolfoodnavigator.mainList

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver

/**
 * ショップリスト ViewModel-Base
 */
abstract class ShopListViewModelBase(
    application: Application
): AndroidViewModel(application),
        LifecycleObserver,
        ShopListViewModelContract


/**
 * ショップリストViewModel-Contract
 */
interface ShopListViewModelContract {

    fun activity(activity: AppCompatActivity)


}