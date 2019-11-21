package com.takhaki.schoolfoodnavigator.Repository

import com.takhaki.schoolfoodnavigator.Model.ShopEntity

fun ShopEntity.toEntity(): ShopEntity {
    return ShopEntity(
        id = id,
        name = name,
        genre = genre,
        userID = userID,
        registerDate = registerDate,
        lastEditedAt = lastEditedAt,
        images = images
    )
}