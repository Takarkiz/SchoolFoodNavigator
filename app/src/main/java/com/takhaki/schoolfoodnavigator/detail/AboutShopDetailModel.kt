package com.takhaki.schoolfoodnavigator.detail

data class AboutShopDetailModel(
    val id: String,
    val name: String,
    val genre: String,
    val score: Float,
    val goodScore: Float,
    val distance: Float,
    val cheep: Float,
    val imageUrl: String?
)
