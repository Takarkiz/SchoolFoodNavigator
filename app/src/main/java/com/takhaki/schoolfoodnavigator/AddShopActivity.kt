package com.takhaki.schoolfoodnavigator

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.takhaki.schoolfoodnavigator.databinding.ActivityAddShopBinding
import kotlinx.android.synthetic.main.activity_add_shop.*

class AddShopActivity : AppCompatActivity() {

    private lateinit var viewModel: AddShopViewModel
    private lateinit var binding: ActivityAddShopBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_shop)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_shop)

        //binding.lifecycleOwner =
        binding.addShopViewModel = viewModel

        actionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.actionbar_title_add_new_shop)

        shopImageView.setOnClickListener {
            didTapAddNewPhoto()
        }
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

    // 画像選択
    private fun didTapAddNewPhoto() {
        Snackbar.make(bottomCoordinator, "未実装です", Snackbar.LENGTH_SHORT).show()
    }
}
