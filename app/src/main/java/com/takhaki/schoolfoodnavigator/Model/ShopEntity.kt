package com.takhaki.schoolfoodnavigator.model

import java.util.*

data class ShopEntity(
    val id: String = "",
    val name: String = "",
    val genre: String = "",
    val userID: String = "",
    val registerDate: Date = Date(),
    val lastEditedAt: Date = Date(),
    val images: List<String> = listOf()
)