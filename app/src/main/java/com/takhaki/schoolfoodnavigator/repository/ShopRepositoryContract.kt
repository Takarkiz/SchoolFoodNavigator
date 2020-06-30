package com.takhaki.schoolfoodnavigator.repository

import com.takhaki.schoolfoodnavigator.entity.ShopEntity
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

interface ShopRepositoryContract {

    /**
     * ショップリスト
     */
    fun getShops(): Flow<List<ShopEntity>>


    /**
     * お店の登録
     */
    fun registerShop(
        shop: ShopEntity,
        imageUrl: String?
    ): Single<Unit>

    /**
     * 指定IDのショップ情報を取得
     *
     * @param id: ショップID
     */
    fun shop(id: String): Flow<ShopEntity>


    /**
     * 日時を更新する
     */
    fun updateEditedDate(shopId: String)

    /**
     * スコアのアップデート
     *
     * @param id: ショップID
     * @param score: 新規スコア
     */
    fun updateScore(id: String, score: Float)

    /**
     * お店を削除する
     *
     * @param id: ショップID
     */
    fun deleteShop(id: String, handler: (Result<String>) -> Unit)
}