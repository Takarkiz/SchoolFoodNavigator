package com.takhaki.schoolfoodnavigator.entity

import java.util.*

data class AssessmentEntity(
    val user: String = "",
    val good: Float = 0f,
    val distance: Float = 0f,
    val cheep: Float = 0f,
    val comment: String = "",
    val createdDate: Date? = null
)