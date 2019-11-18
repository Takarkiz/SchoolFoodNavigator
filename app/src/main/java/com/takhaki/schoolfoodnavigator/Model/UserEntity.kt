package com.takhaki.schoolfoodnavigator.Model

data class UserEntity(
    val id: String,
    val name: String,
    val profImageUrl: String?,
    val favoriteShopList: List<String>
)