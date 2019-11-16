package com.takhaki.schoolfoodnavigator.assesment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.takhaki.schoolfoodnavigator.MainActivity
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.databinding.ActivityAssesmentBinding
import kotlinx.android.synthetic.main.activity_assesment.*

class AssesmentActivity : AppCompatActivity() {

    private lateinit var viewModel: AssesmentViewModel
    private lateinit var binding: ActivityAssesmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assesment)
        val shopName: String = intent.getCharSequenceExtra("shopName").toString()
        val shopId: String = intent.getCharSequenceExtra("shopId").toString()
        actionBar?.setDisplayShowHomeEnabled(true)
        setTitle(shopName)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_assesment
        )

        viewModel = ViewModelProviders.of(this).get(AssesmentViewModel::class.java)
        binding.lifecycleOwner = this
        binding.assesmentViewModel = viewModel

        setUpRadarChart()
        updateRadarChart()

        viewModel.putShopId(shopId)

        foodGoodRating.setOnRatingChangeListener { ratingBar, rating ->
            viewModel.onUpdateGood(rating)
            radarChart.clearValues()
            updateRadarChart()
        }

        distanceRating.setOnRatingChangeListener { ratingBar, rating ->
            viewModel.onUpdateDistance(rating)
            radarChart.clearValues()
            updateRadarChart()
        }

        cheepRating.setOnRatingChangeListener { ratingBar, rating ->
            viewModel.onUpdateCheep(rating)
            radarChart.clearValues()
            updateRadarChart()
        }

        finishAssesment.setOnClickListener {
            viewModel.uploadAssessment { result ->
                if (result.isFailure) {
                    // TODO: - エラー時の処理を加える

                    return@uploadAssessment
                }

                // それ以外はメイン画面に戻る
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

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
            animateXY(1400, 1400, Easing.EaseInOutElastic, Easing.EaseInOutElastic)

            xAxis.apply {
                xOffset = 0f
                yOffset = 0f
                textSize = 10f
                axisMinimum = 0f
                axisMaximum = 4f
                valueFormatter = object : ValueFormatter() {

                    val labels = listOf("おいしさ", "近さ", "安さ")

                    override fun getFormattedValue(value: Float): String {
                        return labels[value.toInt() % labels.size]
                    }
                }
            }

            yAxis.apply {
                axisMinimum = 0f
                axisMaximum = 5f
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
}
