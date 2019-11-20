package com.takhaki.schoolfoodnavigator.Model

import java.util.*

data class ShopEntity(
    val name: String = "",
    val genre: String = "",
    val authorId: String = "",
    val registerDate: Date = Date(),
    val lastEditedAt: Date = Date(),
    val images: List<String> = listOf()
)