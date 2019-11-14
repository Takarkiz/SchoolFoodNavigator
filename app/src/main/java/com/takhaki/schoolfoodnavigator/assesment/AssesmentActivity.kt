package com.takhaki.schoolfoodnavigator.assesment

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
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

        val legend: Legend? = radarChart.legend
        legend?.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT

        // データセット
        val value = listOf(
            viewModel.goodValue.value?.let { it } ?: 0f,
            viewModel.distanceValue.value?.let { it } ?: 0f,
            viewModel.cheepValue.value?.let { it } ?: 0f
        )
        val label = listOf("おいしさ", "近さ", "安さ")
        val entry = ArrayList<RadarEntry>()
        for (i in value.indices) {
            entry.add(RadarEntry(value[i], label[i]))
        }

        /**
         * ラベル
         */
        val dataSet = RadarDataSet(entry, "")
        dataSet.setDrawValues(false)
        val radarData = RadarData(dataSet)
        radarData.setValueFormatter(PercentFormatter())
        radarData.setValueTextSize(20f)
        radarData.setValueTextColor(Color.BLACK)
        radarChart.data = radarData
        radarChart.data.notifyDataChanged()
        radarChart.notifyDataSetChanged()
        radarChart.invalidate()
    }
}
