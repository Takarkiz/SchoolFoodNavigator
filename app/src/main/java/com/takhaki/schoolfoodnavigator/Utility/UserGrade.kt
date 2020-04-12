package com.takhaki.schoolfoodnavigator.Utility

class RewardUtil {

    enum class Grade(val text: String) {
        NORMAL("通り初心者"),
        NORMAL2("一般人"),
        BASIC("ちょっと詳しい人"),
        BASIC2("ちょっとしたグルメ"),
        BASIC3("グルメな人"),
        SUPER("グルメタレント見習い"),
        SUPER2("グルメタレント"),
        SUPER3("通りの達人"),
        NONE("測定不能")
    }

    companion object {

        fun calculateUserRank(point: Int): Grade {
            return when (point) {
                in 0..5 -> Grade.NORMAL
                in 6..15 -> Grade.NORMAL2
                in 16..25 -> Grade.BASIC
                in 26..35 -> Grade.BASIC2
                in 36..50 -> Grade.BASIC3
                in 51..70 -> Grade.SUPER
                in 71..90 -> Grade.SUPER2
                in 90..900 -> Grade.SUPER3
                else -> Grade.NONE
            }
        }

        fun gradeToRange(grade: Grade): IntRange {
            return when (grade) {
                Grade.NORMAL -> 0..5
                Grade.NORMAL2 -> 6..15
                Grade.BASIC -> 16..25
                Grade.BASIC2 -> 26..35
                Grade.BASIC3 -> 36..50
                Grade.SUPER -> 51..70
                Grade.SUPER2 -> 70..90
                Grade.SUPER3 -> 90..900
                else -> 900..10000
            }
        }
    }
}