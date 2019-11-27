package com.takhaki.schoolfoodnavigator.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.assesment.AssesmentActivity
import com.takhaki.schoolfoodnavigator.databinding.ActivityDetailBinding
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        intent.getStringExtra("shopName")?.let { name ->
            setTitle(name)
        }
        val shopId: String = intent.getStringExtra("shopId")
        actionBar?.setDisplayShowHomeEnabled(true)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_detail
        )

        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        binding.lifecycleOwner = this
        binding.detailViewModel = viewModel

        viewModel.putShopId(shopId)
        val adapter = DetailAdapter()
        adapter.shopId = shopId
        viewModel.loadShopDetail()

        viewModel.scoreList.observe(this, androidx.lifecycle.Observer {
            adapter.dataComment = it
        })

        viewModel.shopDetail.observe(this, androidx.lifecycle.Observer {
            adapter.dataAboutShop = it
        })

        scoreListView.adapter = adapter
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        scoreListView.addItemDecoration(itemDecoration)

        addAssessmentFab.setOnClickListener {
            val intent = Intent(this, AssesmentActivity::class.java)
            intent.putExtra("shopId", shopId)
            startActivity(intent)
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_detail_love, menu)
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when (item?.itemId) {
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
