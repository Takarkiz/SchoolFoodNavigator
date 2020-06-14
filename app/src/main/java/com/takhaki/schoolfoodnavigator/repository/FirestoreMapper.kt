package com.takhaki.schoolfoodnavigator.repository

import com.takhaki.schoolfoodnavigator.entity.AssessmentEntity
import com.takhaki.schoolfoodnavigator.entity.Company
import com.takhaki.schoolfoodnavigator.entity.ShopEntity
import com.takhaki.schoolfoodnavigator.entity.UserEntity
import com.takhaki.schoolfoodnavigator.screen.detail.model.AboutShopDetailModel

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
        comment = comment,
        createdDate = createdDate
    )
}

fun UserEntity.toEntity():  UserEntity {
    return UserEntity(
        id = id,
        name = name,
        iconUrl = iconUrl,
        score = score,
        favList = favList
    )
}

fun Company.toEntity(): Company {
    return Company(
        id = id,
        name = name,
        members = members
    )
}

fun ShopEntity.toEntity(assessments: List<AssessmentEntity>): AboutShopDetailModel {
    return AboutShopDetailModel(
        id = id,
        name = name,
        genre = genre,
        score = assessments.map { (it.good + it.cheep + it.distance) / 3 }.average().toFloat(),
        goodScore = assessments.map { it.good }.average().toFloat(),
        distance = assessments.map { it.distance }.average().toFloat(),
        cheep = assessments.map { it.cheep }.average().toFloat(),
        imageUrl = images[0],
        isFavorite = false
    )
}