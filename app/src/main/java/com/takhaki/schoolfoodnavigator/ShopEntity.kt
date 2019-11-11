package com.takhaki.schoolfoodnavigator

import java.util.*

data class ShopEntity(
    val shopName: String,
    val gener: String,
    val authorId: String,
    val registerDate: Date,
    val lastEditedAt: Date
)