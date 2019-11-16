package com.takhaki.schoolfoodnavigator.mainList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.addShop.AddShopActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle("ホーム")

        fab.setOnClickListener {
            val intent = Intent(this, AddShopActivity::class.java)
            startActivity(intent)
        }

        val adapter = ShopListAdapter()
        adapter.data = listOf(ShopListItemModel(
            id = "",
            name = "うま屋",
            shopGenre = "ラーメン屋",
            imageUrl = "https://1.bp.blogspot.com/-Sg-K3HPG64E/XQjudNG_m3I/AAAAAAABTSQ/sD_GacxU0Es9s-qQSxjWSqcvrAuMdRpYwCLcBGAs/s800/ramen_top_tonkotsu.png",
            score = 4.3f),
            ShopListItemModel(
                id = "",
                name = "帯広豚丼王国",
                shopGenre = "豚丼ハウス",
                imageUrl = "https://1.bp.blogspot.com/-ZwMp9KerB-0/XRHQ2s89EFI/AAAAAAABTaY/i1QNxsIg2D8wHRN3M-vDyDTbefi3mLi_wCLcBGAs/s800/food_butadon_obihiro.png",
                score = 3.5f
            ),
            ShopListItemModel(
                id = "",
                name = "帯広豚丼王国",
                shopGenre = "豚丼ハウス",
                imageUrl = "https://1.bp.blogspot.com/-ZwMp9KerB-0/XRHQ2s89EFI/AAAAAAABTaY/i1QNxsIg2D8wHRN3M-vDyDTbefi3mLi_wCLcBGAs/s800/food_butadon_obihiro.png",
                score = 3.5f
            ),
            ShopListItemModel(
                id = "",
                name = "味仙",
                shopGenre = "中華料理",
                imageUrl = "https://1.bp.blogspot.com/-XHNyzCDPEbc/XXRbE5q7V_I/AAAAAAABUrg/FOtz7Xxl2qoBnCeIN5j5IPc8NPlIWYuBQCLcBGAs/s1600/ramen_taiwan.png",
                score = 3.5f
            ),
            ShopListItemModel(
                id = "",
                name = "味国",
                shopGenre = "中華料理",
                imageUrl = "https://1.bp.blogspot.com/-XHNyzCDPEbc/XXRbE5q7V_I/AAAAAAABUrg/FOtz7Xxl2qoBnCeIN5j5IPc8NPlIWYuBQCLcBGAs/s1600/ramen_taiwan.png",
                score = 3.5f
            ),
            ShopListItemModel(
                id = "",
                name = "味鮮",
                shopGenre = "中華料理",
                imageUrl = "https://1.bp.blogspot.com/-XHNyzCDPEbc/XXRbE5q7V_I/AAAAAAABUrg/FOtz7Xxl2qoBnCeIN5j5IPc8NPlIWYuBQCLcBGAs/s1600/ramen_taiwan.png",
                score = 3.5f
            ),
            ShopListItemModel(
                id = "",
                name = "你好シャオツー",
                shopGenre = "中華料理",
                imageUrl = "https://1.bp.blogspot.com/-XHNyzCDPEbc/XXRbE5q7V_I/AAAAAAABUrg/FOtz7Xxl2qoBnCeIN5j5IPc8NPlIWYuBQCLcBGAs/s1600/ramen_taiwan.png",
                score = 3.5f
            ),
            ShopListItemModel(
                id = "",
                name = "海砂利水魚",
                shopGenre = "中華料理",
                imageUrl = "https://1.bp.blogspot.com/-XHNyzCDPEbc/XXRbE5q7V_I/AAAAAAABUrg/FOtz7Xxl2qoBnCeIN5j5IPc8NPlIWYuBQCLcBGAs/s1600/ramen_taiwan.png",
                score = 3.5f
            ))

        shopList.adapter = adapter
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        shopList.addItemDecoration(itemDecoration)
    }

}
