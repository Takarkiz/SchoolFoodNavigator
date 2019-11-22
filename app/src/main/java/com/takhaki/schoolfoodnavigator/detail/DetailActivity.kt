package com.takhaki.schoolfoodnavigator.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.takhaki.schoolfoodnavigator.Model.AssessmentEntity
import com.takhaki.schoolfoodnavigator.Model.ShopEntity
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.databinding.ActivityDetailBinding
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fragment_main_list.*
import java.util.*

class DetailActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        intent.getStringExtra("shopName")?.let { name ->
            setTitle(name)
        }
        //val shopId: String = intent.getStringExtra("shopId")
        actionBar?.setDisplayShowHomeEnabled(true)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_detail
        )

        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        binding.lifecycleOwner = this
        binding.detailViewModel = viewModel

        //viewModel.putShopId(id = shopId)

        val adapter = DetailAdapter()
        adapter.dataAboutShop = ShopEntity(
            id = "",
            name = "COCO壱番亭",
            genre = "カレー",
            images = listOf(),
            registerDate = Date(),
            lastEditedAt = Date(),
            userID = ""
        )

        adapter.dataComment = listOf(
            AssessmentEntity(
                userId = "",
                good = 3.0f,
                distance = 3.0f,
                cheep = 5.0f,
                comment = "ご飯"
            )
        )

        scoreListView.adapter = adapter
//        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
//        shopList.addItemDecoration(itemDecoration)
    }

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
