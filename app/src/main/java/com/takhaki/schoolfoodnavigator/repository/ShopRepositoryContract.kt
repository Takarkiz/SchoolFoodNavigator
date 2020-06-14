package com.takhaki.schoolfoodnavigator.repository

import com.takhaki.schoolfoodnavigator.entity.AssessmentEntity
import com.takhaki.schoolfoodnavigator.entity.ShopEntity
import io.reactivex.Flowable
import io.reactivex.Single

interface ShopRepositoryContract {

    /**
     * ショップリスト
     */
    fun getShops(): Flowable<List<ShopEntity>>


    /**
     * お店の登録
     */
    fun registerShop(
        shop: ShopEntity,
        imageUrl: String?
    ): Single<Unit>


    // 取得

    /**
     * 指定IDのショップ情報を取得
     *
     * @param id: ショップID
     */
    fun shop(id: String): Flowable<ShopEntity>


    /**
     * 指定IDのショップ情報
     *
     * @param id : ショップID
     */
    fun assessments(id: String): Flowable<List<AssessmentEntity>>


    /**
     * 日時を更新する
     */
    fun updateEditedDate(shopId: String)

    /**
     * お店を削除する
     *
     * @param id: ショップID
     */
    fun deleteShop(id: String, handler: (Result<String>) -> Unit)
}