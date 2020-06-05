package com.takhaki.schoolfoodnavigator.screen.addShop

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.Utility.getFileName
import com.takhaki.schoolfoodnavigator.databinding.ActivityAddShopBinding
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_add_shop.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File


class AddShopActivity : AppCompatActivity() {

    companion object {

        /**
         * 遷移用のインテントを作る
         *
         * @param activity アクティビティ
         */
        fun makeIntent(activity: AppCompatActivity): Intent {
            return Intent(activity, AddShopActivity::class.java)
        }
    }

    private lateinit var binding: ActivityAddShopBinding

    private val REQUEST_EXTERNAL_STORAGE = 1000


    private fun didTapAddNewPhoto() {
        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT > 23) {
                requestPermissions(arrayOf(WRITE_EXTERNAL_STORAGE), REQUEST_EXTERNAL_STORAGE)
            }
        } else {
            addNewPhoto()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_shop)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_add_shop
        )

        lifecycle.addObserver(viewModel)
        viewModel.activity(this)
        binding.lifecycleOwner = this
        binding.addShopViewModel = viewModel

        shopImageView.setOnClickListener {
            didTapAddNewPhoto()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.actionbar_title_add_new_shop)

        deleteButton.setOnClickListener {
            viewModel.deletePhoto()
            shopImageView.setImageURI(null)
            shopImageView.setImageResource(R.drawable.add_photo)
        }

        viewModel.isVisibleLoading.observe({ lifecycle }, {
            if (it) {
                loadingAnimation.playAnimation()
                loadingAnimation.visibility = View.VISIBLE
            } else {
                loadingAnimation.pauseAnimation()
                loadingAnimation.visibility = View.GONE
                if (viewModel.isVisibleFinishButton.value!!) {
                    showMaterialDialog()
                }
            }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE -> {

                val resultUri = data?.data ?: viewModel.shopImageUri.value
                resultUri?.let {
                    viewModel.shopImageUri.value = startCrop(it)
                }
            }

            UCrop.REQUEST_CROP -> {
                shopImageView.setImageURI(viewModel.shopImageUri.value)
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            addNewPhoto()
        }
    }

    // Private
    private val viewModel: AddShopViewModel by viewModel()

    // 画像選択
    private fun addNewPhoto() {
        // カメラのIntent作成
        val photoName = System.currentTimeMillis().toString() + ".jpg"
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, photoName)
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        viewModel.shopImageUri.value =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, viewModel.shopImageUri.value)

        val galleryIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE)
        galleryIntent.type = "image/*"

        val intent = Intent.createChooser(
            cameraIntent,
            resources.getString(R.string.add_shop_register_select_image)
        )
        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(galleryIntent))
        startActivityForResult(intent, REQUEST_EXTERNAL_STORAGE)
    }

    private fun startCrop(uri: Uri): Uri {
        val fileName = uri.getFileName(this)
        val resultUri = Uri.fromFile(File(cacheDir, fileName))
        val options = UCrop.Options()
        options.apply {
            setToolbarColor(resources.getColor(R.color.colorPrimary))
            setStatusBarColor(resources.getColor(R.color.colorPrimaryDark))
            setToolbarTitle(resources.getString(R.string.add_shop_register_edit_image))
            setToolbarWidgetColor(resources.getColor(R.color.white))
        }
        UCrop.of(uri, resultUri)
            .withAspectRatio(9f, 9f)
            .withOptions(options)
            .start(this)

        return resultUri
    }

    private fun showMaterialDialog() {
        val animationView = LottieAnimationView(this)
        animationView.setAnimation(R.raw.coin_animation)
        animationView.speed = 1.5f
        animationView.playAnimation()
        MaterialAlertDialogBuilder(this, R.style.reward_alert_dialog)
            .setTitle(resources.getString(R.string.add_shop_register_dialog_text_title))
            .setMessage(resources.getString(R.string.add_shop_register_dialog_text_message))
            .setPositiveButton(
                resources.getString(R.string.add_shop_register_dialog_positive_text)
            ) { _, _ ->
                // 評価画面に遷移する(ここでの遷移時はバックで戻るとお店一覧に戻る)
                viewModel.willMoveToAssessment()
            }
            .setNegativeButton(
                resources.getString(R.string.add_shop_register_dialog_negative_text)
            ) { _, _ ->
                viewModel.backToShopList()
            }
            .setCancelable(false)
            .setView(animationView)
            .show()

    }
}
