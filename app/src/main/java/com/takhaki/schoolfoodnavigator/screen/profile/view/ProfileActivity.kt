package com.takhaki.schoolfoodnavigator.screen.profile.view

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
import com.takhaki.schoolfoodnavigator.Utility.RewardUtil
import com.takhaki.schoolfoodnavigator.databinding.ActivityProfileBinding
import com.takhaki.schoolfoodnavigator.repository.FirestorageRepository
import com.takhaki.schoolfoodnavigator.screen.profile.view_model.ProfileViewModel
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

        viewModel.user.observe(this, Observer { user ->
            userNameTextView.text = user.name
            pointText.text = user.score.toString()
            shogoTextView.text = RewardUtil.calculateUserRank(user.score).text

            user.iconUrl?.let { url ->
                Glide.with(this)
                    .load(FirestorageRepository.getGSReference(url))
                    .placeholder(R.drawable.ic_person_big)
                    .into(iconImageView)
            }
        })

        viewModel.teamName.observe(this, Observer {
            companyTextView.text = it
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
