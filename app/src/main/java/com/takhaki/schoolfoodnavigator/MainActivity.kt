package com.takhaki.schoolfoodnavigator

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.takhaki.schoolfoodnavigator.Repository.UserAuth
import com.takhaki.schoolfoodnavigator.addShop.AddShopActivity
import com.takhaki.schoolfoodnavigator.mainList.MainListFragment
import com.takhaki.schoolfoodnavigator.profile.ProfileActivity
import kotlinx.android.synthetic.main.activity_main.*

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

            override fun createFragment(position: Int): Fragment {
                return MainListFragment.newInstance(position)
            }
        }

        TabLayoutMediator(
            tabLayout,
            viewPager,
            object : TabLayoutMediator.TabConfigurationStrategy {
                override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                    tab.setText(tabLayoutTitle[position])
                }

            }).attach()

        fab.setOnClickListener {
            val intent = Intent(this, AddShopActivity::class.java)
            startActivity(intent)
        }

        val auth = UserAuth()
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
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("UserId", uid)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }
}
