package com.takhaki.schoolfoodnavigator.assesment

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.databinding.ActivityAssesmentBinding
import kotlinx.android.synthetic.main.activity_assesment.*

class AssesmentActivity : AppCompatActivity() {

    private lateinit var viewModel: AssesmentViewModel
    private lateinit var binding: ActivityAssesmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assesment)
        //val shopName: String = intent.getCharSequenceExtra("shopName").toString()
        actionBar?.setDisplayShowHomeEnabled(true)
        //setTitle(shopName)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_assesment
        )

        viewModel = ViewModelProviders.of(this).get(AssesmentViewModel::class.java)
        binding.lifecycleOwner = this
        binding.assesmentViewModel = viewModel

        setUpRadarChart()

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
        val dataSet = RadarDataSet(entry, "お店バロメータ")
        dataSet.apply {
            fillAlpha = 200
            setDrawFilled(true)
            setDrawValues(false)

            // 色の指定

        }

        val radarData = RadarData(dataSet)
        radarData.apply {
            setValueTextSize(9f)
            setValueTextColor(Color.BLACK)
            setDrawValues(true)
        }


        radarChart.apply {
            data = radarData
            legend.isEnabled = false
            description.isEnabled = false
            isRotationEnabled = false
            data.notifyDataChanged()
            notifyDataSetChanged()
            invalidate()

            xAxis.apply {
                xOffset = 0f
                yOffset = 0f
                textSize = 10f
                valueFormatter = object : ValueFormatter() {

                    val labels = listOf("おいしさ", "近さ", "安さ")

                    override fun getFormattedValue(value: Float): String {
                        return labels[value.toInt() % labels.size]
                    }
                }
            }

            yAxis.apply {
                axisMinimum = 0f
                axisMaximum = 4f
                labelCount = 3
                gridColor = R.color.gray
                textColor = R.color.gray
            }
        }

    }
}
