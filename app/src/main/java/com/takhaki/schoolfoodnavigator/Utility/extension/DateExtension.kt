package com.takhaki.schoolfoodnavigator.Utility.extension

import java.text.SimpleDateFormat
import java.util.*

fun Date.normalize(): String {
    val time = this.time
    val current = Date()
    val currentTime = current.time

    // 日時の差を求める
    val oneDayTime = 1000 * 60 * 60 * 24
    val def = currentTime - time

    val defForDay = (def / oneDayTime).toInt()

    //
    when {
        defForDay == 0 -> {
            // 差分の時間を返す
            val oneHourTime = 1000 * 60 * 60
            val defForHour =  (def / oneHourTime).toInt()
            return "$defForHour 時間前"

        }
        defForDay <= 3 -> {
            // 差分の日時を返す
            return "$defForDay 日前"

        }
        else -> {
            // 日付を返す
            return if (current.year == this.year) {
                // 投稿年が同じ場合
                val monthOnlyFormat = SimpleDateFormat("MM/dd", Locale.JAPAN)
                monthOnlyFormat.format(this)
            } else {
                // 投稿年が異なる場合
                val yearContainFormat = SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN)
                yearContainFormat.format(this)
            }
        }
    }
}