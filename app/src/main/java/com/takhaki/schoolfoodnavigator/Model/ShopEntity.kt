package com.takhaki.schoolfoodnavigator.Model

import java.util.*

data class ShopEntity(
    val shopName: String,
    val genre: String,
    val authorId: String,
    val registerDate: Date,
    val lastEditedAt: Date,
    val images: List<String>
)