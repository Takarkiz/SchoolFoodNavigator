package com.takhaki.schoolfoodnavigator.register


import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.takhaki.schoolfoodnavigator.MainActivity
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.Utility.getFileName
import com.takhaki.schoolfoodnavigator.databinding.FragmentIconRegisterBinding
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.fragment_icon_register.*
import java.io.File


class IconRegisterFragment : Fragment() {

    private val REQUEST_EXTERNAL_STORAGE = 1000

    private val args: IconRegisterFragmentArgs by navArgs()
    private lateinit var viewModel: IconRegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentIconRegisterBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_icon_register, container, false
        )

        viewModel = ViewModelProviders.of(this).get(IconRegisterViewModel::class.java)
        binding.iconRegisterViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.putUserName(args.name)
        return inflater.inflate(R.layout.fragment_icon_register, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        iconView.setOnClickListener {
            didTapSelectUserPhoto()
        }

        finishRegisterButton.setOnClickListener {
            loadingAnimationView.visibility = View.VISIBLE
            loadingAnimationView.playAnimation()
            context?.let { context ->
                viewModel.saveCompanyID(args.teamID, context)
            }
            viewModel.createUser()
            finishRegisterButton.isEnabled = false
        }

        viewModel.isCompletedUserData.observe(this, Observer { complete ->
            if (complete) {
                loadingAnimationView.visibility = View.GONE
                loadingAnimationView.pauseAnimation()
                finishRegisterButton.isEnabled = true
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                context?.startActivity(intent)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE -> {

                val resultUri = if (data?.data != null) data.data else viewModel.iconImageUri.value

                resultUri?.let {
                    startCrop(it)?.let { uri ->
                        viewModel.putIconImage(uri)
                    }

                }
            }

            UCrop.REQUEST_CROP -> {
                iconView.setImageURI(viewModel.iconImageUri.value)
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

    fun didTapSelectUserPhoto() {
        context?.let { context ->

            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                if (Build.VERSION.SDK_INT > 23) {
                    requestPermissions(
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        REQUEST_EXTERNAL_STORAGE
                    )
                }
            } else {
                addNewPhoto()
            }
        }
    }

    // 画像選択
    private fun addNewPhoto() {
        // カメラのIntent作成
        val photoName = System.currentTimeMillis().toString() + ".jpg"
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, photoName)
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        context?.contentResolver?.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )?.let { uri ->
            viewModel.putIconImage(uri)
        }

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, viewModel.iconImageUri.value)

        val galleryIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE)
        galleryIntent.type = "image/*"

        val intent = Intent.createChooser(cameraIntent, "画像の選択")
        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(galleryIntent))
        startActivityForResult(intent, REQUEST_EXTERNAL_STORAGE)
    }

    private fun startCrop(uri: Uri): Uri? {

        val options = UCrop.Options()
        options.setCircleDimmedLayer(true)

        context?.let { context ->
            val fileName = uri.getFileName(context)
            val resultUri = Uri.fromFile(File(context.cacheDir, fileName!!))
            UCrop.of(uri, resultUri)
                .withAspectRatio(9f, 9f)
                .withOptions(options)
                .start(context, this, UCrop.REQUEST_CROP)

            return resultUri
        }

        return null
    }
}
