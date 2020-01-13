package com.takhaki.schoolfoodnavigator.profile

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

class ProfileActivity : AppCompatActivity() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setTitle("プロフィール")

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_profile
        )

        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        binding.lifecycleOwner = this
        binding.profileViewModel = viewModel

        UserAuth(this).currentUser?.uid?.let {
            viewModel.updateUserProfile(it)
        }

        viewModel.userImageUrl.observe(this, Observer {
            val storage = FirestorageRepository("User")
            Glide.with(this)
                .load(storage.getGSReference(it))
                .placeholder(R.drawable.ic_default_user)
                .into(iconImageView)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
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
