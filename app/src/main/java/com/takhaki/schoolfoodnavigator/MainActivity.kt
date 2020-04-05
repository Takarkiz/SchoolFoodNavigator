package com.takhaki.schoolfoodnavigator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.takhaki.schoolfoodnavigator.Repository.UserAuth
import com.takhaki.schoolfoodnavigator.mainList.MainListFragment
import com.takhaki.schoolfoodnavigator.mainList.ShopListViewModel
import com.takhaki.schoolfoodnavigator.mainList.ShopListViewModelBase
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayoutTitle: List<String> = listOf(
            this.getString(R.string.tab_item_title_latest),
            this.getString(R.string.tab_item_title_ranking),
            this.getString(R.string.tab_title_favorite)
        )

        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = tabLayoutTitle.size

            override fun createFragment(position: Int): Fragment
                = MainListFragment.newInstance(position)
        }

        TabLayoutMediator(
            tabLayout,
            viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = tabLayoutTitle[position]
            }).attach()

        fab.setOnClickListener {
            viewModel.didTapAddFabIcon()
        }

        val auth = UserAuth(this)
        val uid = auth.currentUser?.uid
        auth.currentUserIconUrl { result ->
            if (result.isSuccess) {
                result.getOrNull()?.let { url ->
                    Glide.with(this)
                        .load(url)
                        .placeholder(R.drawable.ic_nav_icon_mypage)
                        .into(iconImage)
                }
            }
        }

        iconImage.setOnClickListener {
            uid?.let {
                viewModel.didTapOwnProfileIcon(it)
            }
        }

        lifecycle.addObserver(viewModel)
        viewModel.activity(this)
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }

    private val viewModel: ShopListViewModelBase by viewModel()

}
