package com.takhaki.schoolfoodnavigator.model

data class AssessmentEntity(
    val user: String = "",
    val good: Float = 0f,
    val distance: Float = 0f,
    val cheep: Float = 0f,
    val comment: String = ""
)