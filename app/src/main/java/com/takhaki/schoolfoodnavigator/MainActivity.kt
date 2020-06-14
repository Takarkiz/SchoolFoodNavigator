package com.takhaki.schoolfoodnavigator

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.takhaki.schoolfoodnavigator.repository.FirestorageRepository
import com.takhaki.schoolfoodnavigator.screen.mainList.ShopListViewModelBase
import com.takhaki.schoolfoodnavigator.screen.mainList.view.MainListFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    companion object {

        /**
         * 繊維用のインテントを作る
         */

        fun makeIntent(activity: AppCompatActivity): Intent {
            return Intent(activity, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
    }

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

            override fun createFragment(position: Int): Fragment =
                MainListFragment.newInstance(position)
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

        lifecycle.addObserver(viewModel)
        viewModel.activity(this)

        viewModel.userIconUrl.observe({ lifecycle }, { url ->
            Glide.with(this)
                .load(FirestorageRepository.getGSReference(url))
                .placeholder(R.drawable.ic_nav_icon_mypage)
                .into(iconImage)
        })

        iconImage.setOnClickListener {
            viewModel.didTapOwnProfileIcon()
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }

    private val viewModel: ShopListViewModelBase by viewModel()

}
