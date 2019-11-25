package com.takhaki.schoolfoodnavigator.detail

data class AboutShopDetailModel(
    val id: String = "",
    val name: String = "",
    val genre: String = "",
    val score: Float = 0.0f,
    val goodScore: Float = 0.0f,
    val distance: Float = 0.0f,
    val cheep: Float = 0.0f,
    val imageUrl: String? = null
)
