package com.takhaki.schoolfoodnavigator.Repository

import com.takhaki.schoolfoodnavigator.Model.AssessmentEntity
import com.takhaki.schoolfoodnavigator.Model.ShopEntity
import com.takhaki.schoolfoodnavigator.Model.UserEntity

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
        user = user,
        good = good,
        distance = distance,
        cheep = cheep,
        comment = comment
    )
}

fun UserEntity.toEntity():  UserEntity {
    return UserEntity(
        id = id,
        name = name,
        profImageUrl = profImageUrl,
        navScore = navScore,
        favList = favList
    )
}