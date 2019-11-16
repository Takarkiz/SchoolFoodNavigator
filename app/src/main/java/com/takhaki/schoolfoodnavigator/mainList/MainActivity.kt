package com.takhaki.schoolfoodnavigator.mainList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.addShop.AddShopActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            val intent = Intent(this, AddShopActivity::class.java)
            startActivity(intent)
        }

        val adapter = ShopListAdapter()
        adapter.data = listOf()
    }

}
