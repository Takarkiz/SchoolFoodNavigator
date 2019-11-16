package com.takhaki.schoolfoodnavigator.mainList


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.takhaki.schoolfoodnavigator.R
import kotlinx.android.synthetic.main.fragment_main_list.*


class MainListFragment : Fragment() {

    companion object {
        fun newInstance(tabNumber: Int): MainListFragment = MainListFragment().apply {
            arguments = Bundle().apply {
                putString("text", tabNumber.toString())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = ShopListAdapter()
        adapter.data = listOf(
            ShopListItemModel(
                id = "",
                name = "うま屋",
                shopGenre = "ラーメン屋",
                imageUrl = "https://1.bp.blogspot.com/-Sg-K3HPG64E/XQjudNG_m3I/AAAAAAABTSQ/sD_GacxU0Es9s-qQSxjWSqcvrAuMdRpYwCLcBGAs/s800/ramen_top_tonkotsu.png",
                score = 4.3f
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
            ),
            ShopListItemModel(
                id = "",
                name = "鉄板キング",
                shopGenre = "鉄板料理",
                imageUrl = "https://3.bp.blogspot.com/-EAlmQsmNDec/WUdYXNJau9I/AAAAAAABE7A/p00qx2Iz_HQaf_t_Dp9lfpmKWlptY0j0wCLcBGAs/s800/food_omusoba.png",
                score = 5.0f
            )
        )

        shopList.adapter = adapter
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        shopList.addItemDecoration(itemDecoration)
    }
}
