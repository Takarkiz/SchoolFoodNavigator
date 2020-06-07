package com.takhaki.schoolfoodnavigator.model

data class UserEntity(
    val id: String = "",
    val name: String = "",
    val iconUrl: String? = null,
    val score: Int = 0,
    val favList: List<String> = listOf()
)