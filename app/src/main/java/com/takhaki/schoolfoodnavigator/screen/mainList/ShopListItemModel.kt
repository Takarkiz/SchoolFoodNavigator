package com.takhaki.schoolfoodnavigator.screen.mainList

import java.util.*

data class ShopListItemModel(
    val id: String,
    val name: String,
    val shopGenre: String,
    val imageUrl: String?,
    val editedAt: Date,
    val isFavorite: Boolean,
    val score: Float
)