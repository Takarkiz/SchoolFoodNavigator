package com.takhaki.schoolfoodnavigator.Model

data class UserEntity(
    val id: String,
    val name: String,
    val profImageUrl: String?,
    val navScore: Int,
    val favoriteShopList: List<String>
)