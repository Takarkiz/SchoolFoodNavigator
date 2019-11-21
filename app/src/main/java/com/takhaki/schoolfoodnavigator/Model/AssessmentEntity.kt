package com.takhaki.schoolfoodnavigator.Model

data class AssessmentEntity(
    val userId: String,
    val good: Float,
    val distance: Float,
    val cheep: Float,
    val totalScore: Float,
    val comment: String
)