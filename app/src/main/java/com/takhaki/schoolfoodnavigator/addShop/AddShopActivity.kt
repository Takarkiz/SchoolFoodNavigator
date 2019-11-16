package com.takhaki.schoolfoodnavigator.addShop

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.takhaki.schoolfoodnavigator.assesment.AssesmentActivity
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.databinding.ActivityAddShopBinding
import com.takhaki.schoolfoodnavigator.Utility.getFileName
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_add_shop.*
import java.io.File


class AddShopActivity : AppCompatActivity() {

    private lateinit var viewModel: AddShopViewModel
    private lateinit var binding: ActivityAddShopBinding

    private val REQUEST_EXTERNAL_STORAGE = 1000


    fun didTapAddNewPhoto() {
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

        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_add_shop
        )
        viewModel = ViewModelProviders.of(this).get(AddShopViewModel::class.java)

        binding.lifecycleOwner = this
        binding.addShopViewModel = viewModel

        shopImageView.setOnClickListener {
            didTapAddNewPhoto()
        }

        actionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.actionbar_title_add_new_shop)

        deleteButton.setOnClickListener {
            viewModel.deletePhoto()
            shopImageView.setImageURI(null)
            shopImageView.setImageResource(R.drawable.add_photo)
        }

        viewModel.willIntentAssesment.observe(this, Observer { shouldShowDialog ->
            if (shouldShowDialog) showDialog()
        })
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE -> {


                val resultUri = if (data != null) data.data else viewModel.shopImageUri.value

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
        galleryIntent.type = "image/jpeg"

        val intent = Intent.createChooser(cameraIntent, "画像の選択")
        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(galleryIntent))
        startActivityForResult(intent, REQUEST_EXTERNAL_STORAGE)
    }

    private fun startCrop(uri: Uri): Uri {
        val fileName = uri.getFileName(this)
        val resultUri = Uri.fromFile(File(cacheDir, fileName))
        UCrop.of(uri, resultUri)
            .withAspectRatio(9f, 9f)
            .start(this)

        return resultUri
    }


    private val okListener = DialogInterface.OnClickListener { dialog, which ->
        viewModel.toIntentAssesment(shoudGoAssesment = false)

        // 評価画面に遷移する(ここでの遷移時はバックで戻るとお店一覧に進む)
        val intent = Intent(this, AssesmentActivity::class.java)
        intent.putExtra("shopName", viewModel.shopName.value)
        intent.putExtra("shopId", viewModel.shopId.value)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
    private val cancelListener = DialogInterface.OnClickListener { dialog, which ->
        viewModel.toIntentAssesment(shoudGoAssesment = false)
        finish()
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("登録が完了しました")
            .setMessage("今回登録したお店の評価を続けますか？")
            .setPositiveButton("はい", okListener)
            .setNegativeButton("いいえ", cancelListener).show()
    }
}
