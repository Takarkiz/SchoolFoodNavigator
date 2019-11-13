package com.takhaki.schoolfoodnavigator

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.takhaki.schoolfoodnavigator.databinding.ActivityAddShopBinding
import kotlinx.android.synthetic.main.activity_add_shop.*


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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_shop)
        viewModel = ViewModelProviders.of(this).get(AddShopViewModel::class.java)

        binding.lifecycleOwner = this
        binding.addShopViewModel = viewModel

        shopImageView.setOnClickListener {
            didTapAddNewPhoto()
        }

        actionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.actionbar_title_add_new_shop)

        deleteButton.setOnClickListener {
            viewModel.isVisibleDeleteButton.value = false
            deleteButton.setImageURI(null)
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

    private var m_uri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_EXTERNAL_STORAGE) {

            val resultUri = if (data != null) data.data else m_uri

            resultUri?.let {
                MediaScannerConnection.scanFile(
                    this,
                    arrayOf(it.path),
                    arrayOf("image/jpeg"),
                    null
                )

                shopImageView.setImageURI(resultUri)
                viewModel.isVisibleDeleteButton.value = true
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
        m_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, m_uri)

        val galleryIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE)
        galleryIntent.setType("image/jpeg")

        val intent = Intent.createChooser(cameraIntent, "画像の選択")
        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(galleryIntent))
        startActivityForResult(intent, REQUEST_EXTERNAL_STORAGE)
    }
}
