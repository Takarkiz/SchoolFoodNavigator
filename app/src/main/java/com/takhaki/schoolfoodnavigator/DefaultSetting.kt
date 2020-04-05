package com.takhaki.schoolfoodnavigator

import android.app.Application
import com.takhaki.schoolfoodnavigator.module.navigatorModule
import com.takhaki.schoolfoodnavigator.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
class DefaultSetting : Application() {

    override fun onCreate() {
        super.onCreate()
        setupDI()

    }

    private fun setupDI() {
        startKoin {
            androidContext(this@DefaultSetting)

            modules(listOf(
                viewModelModule,
                navigatorModule
            ))
        }
    }
}