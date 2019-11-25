package com.takhaki.schoolfoodnavigator.mainList

import java.util.*

data class ShopListItemModel(
    val id: String,
    val name: String,
    val shopGenre: String,
    val imageUrl: String?,
    val editedAt: Date,
    val score: Float
)