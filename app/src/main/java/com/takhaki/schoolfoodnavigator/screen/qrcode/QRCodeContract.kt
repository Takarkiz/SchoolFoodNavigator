package com.takhaki.schoolfoodnavigator.screen.qrcode

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import com.takhaki.schoolfoodnavigator.BaseNavigator

abstract class QRCodeViewModelBase(
    application: Application
) : AndroidViewModel(application),
    LifecycleObserver,
    QRCodeViewModelContract

interface QRCodeViewModelContract {

    fun activity(activity: AppCompatActivity)

    val companyCode: LiveData<Int>

    val isCodeLoading: LiveData<Boolean>

}

abstract class QRCodeNavigatorAbstract : BaseNavigator()