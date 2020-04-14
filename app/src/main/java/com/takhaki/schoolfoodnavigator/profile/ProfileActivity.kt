package com.takhaki.schoolfoodnavigator.profile

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.firebase.storage.StorageReference
import com.takhaki.schoolfoodnavigator.DataBinderMapperImpl
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.Repository.FirestorageRepository
import com.takhaki.schoolfoodnavigator.Repository.UserAuth
import com.takhaki.schoolfoodnavigator.databinding.ActivityProfileBinding
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
            val storage = FirestorageRepository(FirestorageRepository.StorageTypes.USER)
            Glide.with(this)
                .load(storage.getGSReference(it))
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
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
}
