package com.takhaki.schoolfoodnavigator.Repository

import com.takhaki.schoolfoodnavigator.Model.AssessmentEntity
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

fun AssessmentEntity.toEntity(): AssessmentEntity {
    return AssessmentEntity(
        userId = userId,
        good = good,
        distance = distance,
        cheep = cheep,
        comment = comment
    )
}