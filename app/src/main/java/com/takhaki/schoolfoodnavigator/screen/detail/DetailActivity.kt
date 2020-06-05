package com.takhaki.schoolfoodnavigator.screen.detail

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.databinding.ActivityDetailBinding
import kotlinx.android.synthetic.main.activity_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailActivity : AppCompatActivity() {

    companion object {

        /**
         * 遷移用のインテントを作る
         *
         * @param activity アクティビティ
         */
        fun makeIntent(activity: AppCompatActivity, shopId: String, name: String): Intent {
            return Intent(activity, DetailActivity::class.java).apply {
                putExtra(EXTRA_KEY_SHOP_ID, shopId)
                putExtra(EXTRA_KEY_SHOP_NAME, name)
            }
        }

        private const val EXTRA_KEY_SHOP_ID = "shopId"
        private const val EXTRA_KEY_SHOP_NAME = "name"
    }

    private val viewModel: DetailViewModel by viewModel {
        val shopId = intent.extras?.getString(EXTRA_KEY_SHOP_ID)
        parametersOf(shopId)
    }
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        intent.getStringExtra(EXTRA_KEY_SHOP_NAME)?.let { name ->
            supportActionBar?.setDisplayShowTitleEnabled(true)
            supportActionBar?.title = name
        }
        val shopId: String = intent.getStringExtra(EXTRA_KEY_SHOP_ID)
        actionBar?.setDisplayShowHomeEnabled(true)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_detail
        )

        viewModel.activity(this)
        lifecycle.addObserver(viewModel)
        binding.lifecycleOwner = this
        binding.detailViewModel = viewModel

        val adapter = DetailAdapter(this, viewModel)
        adapter.shopId = shopId

        viewModel.scoreList.observe(this, androidx.lifecycle.Observer {
            adapter.dataComment = it
        })

        viewModel.shopDetail.observe(this, androidx.lifecycle.Observer {
            adapter.dataAboutShop = it
        })

        scoreListView.adapter = adapter
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        scoreListView.addItemDecoration(itemDecoration)
        scoreListView.clearOnScrollListeners()
        scoreListView.addOnScrollListener(scrollListener)

        viewModel.hasCurrentUserComment.observe(this, Observer { hasCurrentUser ->
            addAssessmentFab.isVisible = !hasCurrentUser
        })

        addAssessmentFab.setOnClickListener {
            viewModel.didTapAddFab()
        }
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

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

            when (newState) {
                RecyclerView.SCROLL_STATE_IDLE -> {
                    viewModel.hasCurrentUserComment.value?.let {
                        if (!it) addAssessmentFab.show()
                    }
                }
                else -> addAssessmentFab.hide()
            }
            super.onScrollStateChanged(recyclerView, newState)
        }
    }
}
