package com.takhaki.schoolfoodnavigator.Repository

import com.takhaki.schoolfoodnavigator.Model.ShopEntity

fun ShopEntity.toEntity(): ShopEntity {
    return ShopEntity(
        name = name,
        genre = genre,
        authorId = authorId,
        registerDate = registerDate,
        lastEditedAt = lastEditedAt,
        images = images
    )
}