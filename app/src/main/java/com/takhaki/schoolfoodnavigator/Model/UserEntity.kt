package com.takhaki.schoolfoodnavigator.Model

data class UserEntity(
    val id: String = "",
    val name: String = "",
    val profImageUrl: String? = null,
    val navScore: Int = 0,
    val favList: List<String> = listOf()
)