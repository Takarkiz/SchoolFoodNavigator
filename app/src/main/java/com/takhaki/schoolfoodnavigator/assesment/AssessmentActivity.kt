package com.takhaki.schoolfoodnavigator.assesment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.databinding.DataBindingUtil
import com.airbnb.lottie.LottieAnimationView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.databinding.ActivityAssesmentBinding
import kotlinx.android.synthetic.main.activity_assesment.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AssessmentActivity : AppCompatActivity() {

    companion object {

        /**
         * 遷移用のインテントを作る
         *
         * @param activity: アクティビティ
         * @param id: ショップID
         * @param name: ショップ名
         */
        fun makeIntent(activity: AppCompatActivity, id: String, name: String): Intent {
            return Intent(activity, AssessmentActivity::class.java).apply {
                putExtra(EXTRA_KEY_SHOP_ID, id)
                putExtra(EXTRA_KEY_SHOP_NAME, name)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
        }

        private const val EXTRA_KEY_SHOP_ID = "shopId"
        private const val EXTRA_KEY_SHOP_NAME = "shopName"
    }

    private val viewModel: AssessmentViewModel by viewModel {
        val shopId = intent.extras?.getString(EXTRA_KEY_SHOP_ID) ?: ""
        parametersOf(shopId)
    }
    private lateinit var binding: ActivityAssesmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assesment)
        actionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = intent.getCharSequenceExtra(EXTRA_KEY_SHOP_NAME) ?: ""

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_assesment
        )

        viewModel.activity(this)
        lifecycle.addObserver(viewModel)
        binding.lifecycleOwner = this
        binding.assesmentViewModel = viewModel

        setUpRadarChart()
        updateRadarChart()

        foodGoodRating.setOnRatingChangeListener { ratingBar, rating ->
            viewModel.onUpdateGood(rating)
            setUpRadarChart()
            updateRadarChart()
        }

        distanceRating.setOnRatingChangeListener { ratingBar, rating ->
            viewModel.onUpdateDistance(rating)
            setUpRadarChart()
            updateRadarChart()
        }

        cheepRating.setOnRatingChangeListener { ratingBar, rating ->
            viewModel.onUpdateCheep(rating)
            setUpRadarChart()
            updateRadarChart()
        }

        finishAssesment.setOnClickListener {
            viewModel.uploadAssessment { result ->
                if (result.isFailure) {
                    // TODO: - エラー時の処理を加える

                    return@uploadAssessment
                }

                showMaterialDialog()
            }
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

    private fun setUpRadarChart() {

        radarChart.apply {

            legend.isEnabled = false
            description.isEnabled = false
            isRotationEnabled = false

            xAxis.apply {
                xOffset = 0f
                yOffset = 0f
                textSize = 10f
                axisMinimum = 0f
                axisMaximum = 3.5f
                valueFormatter = object : ValueFormatter() {

                    val labels = listOf(
                        resources.getString(R.string.assessment_category_good),
                        resources.getString(R.string.assessment_category_near),
                        resources.getString(R.string.assessment_category_near)
                    )

                    override fun getFormattedValue(value: Float): String {
                        return labels[value.toInt() % labels.size]
                    }
                }
            }

            yAxis.apply {
                axisMinimum = 0f
                axisMaximum = 3.5f
                labelCount = 3
                gridColor = R.color.gray
                textColor = R.color.gray
            }
        }

    }

    private fun updateRadarChart() {

        // データセット
        val value = listOf(
            viewModel.goodValue.value?.let { it } ?: 0f,
            viewModel.distanceValue.value?.let { it } ?: 0f,
            viewModel.cheepValue.value?.let { it } ?: 0f
        )

        val entry = ArrayList<RadarEntry>()
        for (i in value.indices) {
            entry.add(RadarEntry(value[i]))
        }

        /**
         * ラベル
         */
        val dataSet = RadarDataSet(entry, "")
        dataSet.apply {
            fillAlpha = 200
            setDrawFilled(true)
            setDrawValues(false)

        }

        val radarData = RadarData(dataSet)
        radarData.apply {
            setValueTextSize(9f)
            setValueTextColor(Color.BLACK)
            setDrawValues(true)
        }

        radarChart.apply {
            animateY(500, Easing.EaseInOutElastic)
            data = radarData
            data.notifyDataChanged()
            notifyDataSetChanged()
            invalidate()
        }
    }

    private fun showMaterialDialog() {
        val animationView = LottieAnimationView(this)
        animationView.setAnimation(R.raw.coin_animation)
        animationView.speed = 1.5f
        animationView.playAnimation()
        MaterialAlertDialogBuilder(this, R.style.reward_alert_dialog)
            .setTitle(resources.getString(R.string.assessment_dialog_title_text))
            .setPositiveButton(resources.getString(R.string.assessment_dialog_positive_text)) { _, _ ->
                backToHome()
            }
            .setCancelable(false)
            .setView(animationView)
            .show()

    }

    private fun backToHome() {
        // それ以外はメイン画面に戻る
        viewModel.finishUpload()
    }
}
