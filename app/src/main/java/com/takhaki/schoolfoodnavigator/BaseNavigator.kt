package com.takhaki.schoolfoodnavigator

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import java.lang.ref.WeakReference

open class BaseNavigator {

    var weakActivity: WeakReference<AppCompatActivity>? = null

    fun finish() {
        weakActivity?.get()?.finish()
    }
}