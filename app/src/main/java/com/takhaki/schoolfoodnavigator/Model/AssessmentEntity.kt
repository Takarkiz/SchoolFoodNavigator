package com.takhaki.schoolfoodnavigator.Model

data class AssessmentEntity(
    val userId: String = "",
    val good: Float = 0f,
    val distance: Float = 0f,
    val cheep: Float = 0f,
    val comment: String = ""
)