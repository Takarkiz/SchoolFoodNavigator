package com.takhaki.schoolfoodnavigator.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.takhaki.schoolfoodnavigator.R

class RegisterUserActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }
}
