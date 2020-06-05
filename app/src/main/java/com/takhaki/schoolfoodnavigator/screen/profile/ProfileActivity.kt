package com.takhaki.schoolfoodnavigator.screen.profile

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.databinding.ActivityProfileBinding
import com.takhaki.schoolfoodnavigator.repository.FirestorageRepository
import com.takhaki.schoolfoodnavigator.repository.StorageTypes
import kotlinx.android.synthetic.main.activity_profile.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProfileActivity : AppCompatActivity() {

    companion object {

        /**
         * 遷移用のインテントを作る
         */
        fun makeIntent(activity: AppCompatActivity, userId: String): Intent {
            return Intent(activity, ProfileActivity::class.java).apply {
                putExtra(EXTRA_KEY_USER_ID, userId)
            }
        }

        private const val EXTRA_KEY_USER_ID = "user_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = "プロフィール"

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_profile
        )

        binding.lifecycleOwner = this
        binding.profileViewModel = viewModel
        viewModel.activity(this)
        lifecycle.addObserver(viewModel)

        viewModel.userImageUrl.observe(this, Observer {
            Glide.with(this)
                .load(FirestorageRepository.getGSReference(it))
                .placeholder(R.drawable.ic_default_user)
                .into(iconImageView)
        })

        aboutRewardText.setOnClickListener {
            viewModel.didTapShowRewardDetail()
        }

        companyTextView.setOnClickListener {
            viewModel.didTapCompanyText()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_item_profile_delete -> {
                deleteUser()
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private val viewModel: ProfileViewModel by viewModel {
        val uid = intent.extras?.getString(EXTRA_KEY_USER_ID)
        parametersOf(uid)
    }
    private lateinit var binding: ActivityProfileBinding

    private fun deleteUser() {
        MaterialAlertDialogBuilder(this)
            .setTitle("ユーザー情報を削除します")
            .setMessage("削除されたデータには2度とアクセスできませんがよろしいですか？")
            .setPositiveButton("はい") { dialog, which ->
                viewModel.didTapDeleteUser()
            }
            .setNegativeButton("キャンセル", null)
            .show()
    }
}
