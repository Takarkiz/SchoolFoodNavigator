package com.takhaki.schoolfoodnavigator.qrcode.view_model

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.takhaki.schoolfoodnavigator.Model.CompanyData
import com.takhaki.schoolfoodnavigator.qrcode.QRCodeNavigatorAbstract
import com.takhaki.schoolfoodnavigator.qrcode.QRCodeViewModelBase
import java.lang.ref.WeakReference

class QRCodeViewModel(
    application: Application,
    private val navigator: QRCodeNavigatorAbstract
) : QRCodeViewModelBase(application) {

    override fun activity(activity: AppCompatActivity) {
        navigator.weakActivity = WeakReference(activity)
    }

    override val companyCode: LiveData<Int>
        get() = _companyCode
    override val isCodeLoading: LiveData<Boolean>
        get() = _isCodeLoading


    // LifecycleObserver
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        loadingCompanyCode()
    }

    // Private
    private val _companyCode = MutableLiveData<Int>()
    private val _isCodeLoading = MutableLiveData(true)

    private fun loadingCompanyCode() {
        val companyId = CompanyData.getCompanyId(getApplication())
        _companyCode.postValue(companyId)
        _isCodeLoading.postValue(false)
    }
}