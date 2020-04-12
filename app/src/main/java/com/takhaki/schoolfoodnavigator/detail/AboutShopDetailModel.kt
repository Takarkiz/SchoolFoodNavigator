package com.takhaki.schoolfoodnavigator.detail

data class AboutShopDetailModel(
    val id: String = "",
    val name: String = "",
    val genre: String = "",
    val score: Float = 0.0f,
    val goodScore: Float = 0.0f,
    val distance: Float = 0.0f,
    val cheep: Float = 0.0f,
    val imageUrl: String? = null,
    val isFavorite: Boolean = false
)

data class CommentDetailModel(
    val id: String = "",
    val name: String = "",
    val userIcon: String? = null,
    val gScore: Float = 0f,
    val dScore: Float = 0f,
    val cScore: Float = 0f,
    val comment: String = ""
)
