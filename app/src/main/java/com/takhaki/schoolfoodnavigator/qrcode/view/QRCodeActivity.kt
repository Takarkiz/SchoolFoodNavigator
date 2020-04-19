package com.takhaki.schoolfoodnavigator.qrcode.view

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.qrcode.view_model.QRCodeViewModel
import kotlinx.android.synthetic.main.activity_q_r_code.*
import org.koin.android.viewmodel.ext.android.viewModel


class QRCodeActivity : AppCompatActivity() {

    companion object {

        /**
         * 遷移用のインテントを作る
         */
        fun makeIntent(activity: AppCompatActivity): Intent {
            return Intent(activity, QRCodeActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_q_r_code)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = resources.getString(R.string.qrcode_title)

        viewModel.activity(this)
        lifecycle.addObserver(viewModel)

        showQRCode()
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

    // Private

    private val viewModel: QRCodeViewModel by viewModel<QRCodeViewModel>()

    private fun showQRCode() {
        viewModel.companyCode.observe({ lifecycle }, { code ->
            codeContentText.text = code.toString()

            try {
                val qrEncoder = BarcodeEncoder()
                val bitmap: Bitmap =
                    qrEncoder.encodeBitmap(code.toString(), BarcodeFormat.QR_CODE, 280, 280)
                qrImageView.setImageBitmap(bitmap)
            } catch (e: Exception) {
                Toast.makeText(this, resources.getString(R.string.qrcode_error_show), Toast.LENGTH_SHORT).show()
            }
        })
    }
}
