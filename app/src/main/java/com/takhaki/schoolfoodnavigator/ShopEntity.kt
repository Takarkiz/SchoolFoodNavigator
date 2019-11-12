package com.takhaki.schoolfoodnavigator

import java.util.*

data class ShopEntity(
    val shopName: String,
    val genre: String,
    val authorId: String,
    val registerDate: Date,
    val lastEditedAt: Date
)