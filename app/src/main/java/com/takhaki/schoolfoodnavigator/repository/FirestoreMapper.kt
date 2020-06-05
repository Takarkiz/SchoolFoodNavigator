package com.takhaki.schoolfoodnavigator.repository

import com.takhaki.schoolfoodnavigator.Model.AssessmentEntity
import com.takhaki.schoolfoodnavigator.Model.ShopEntity
import com.takhaki.schoolfoodnavigator.Model.UserEntity
import com.takhaki.schoolfoodnavigator.detail.AboutShopDetailModel
import com.takhaki.schoolfoodnavigator.detail.CommentDetailModel
import org.w3c.dom.Comment

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
        iconUrl = iconUrl,
        score = score,
        favList = favList
    )
}

fun ShopEntity.toEntity(assessments: List<AssessmentEntity>): AboutShopDetailModel {
    return AboutShopDetailModel(
        id = id,
        name = name,
        genre = genre,
        score = assessments.map{ (it.good + it.cheep + it.distance)/3 }.average().toFloat(),
        goodScore = assessments.map { it.good }.average().toFloat(),
        distance = assessments.map { it.distance }.average().toFloat(),
        cheep = assessments.map { it.cheep }.average().toFloat(),
        imageUrl = images[0],
        isFavorite = false
    )
}