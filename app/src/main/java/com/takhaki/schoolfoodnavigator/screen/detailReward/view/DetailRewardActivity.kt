package com.takhaki.schoolfoodnavigator.screen.detailReward.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.screen.detailReward.viewModel.DetailRewardViewModel
import kotlinx.android.synthetic.main.activity_detail_reward.*
import org.koin.android.viewmodel.ext.android.viewModel

class DetailRewardActivity : AppCompatActivity() {

    companion object {

        /**
         * 遷移用のインテントを作成する
         */
        fun makeIntent(activity: AppCompatActivity): Intent {
            return Intent(activity, DetailRewardActivity::class.java).apply {
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_reward)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = resources.getString(R.string.detail_reward_title)

        val adapter = RewardListAdapter()
        recyclerView.adapter = adapter

        viewModel.activity(this)
        lifecycle.addObserver(viewModel)

        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(itemDecoration)

        viewModel.userGrade.observe({ lifecycle }, {
            adapter.setUserGrade(it)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Private

    private val viewModel: DetailRewardViewModel by viewModel()
}
