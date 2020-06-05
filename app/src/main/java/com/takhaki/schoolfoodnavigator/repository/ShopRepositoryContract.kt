package com.takhaki.schoolfoodnavigator.repository

import android.content.Context
import android.net.Uri
import com.takhaki.schoolfoodnavigator.Model.AssessmentEntity
import com.takhaki.schoolfoodnavigator.Model.ShopEntity
import io.reactivex.Flowable

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
        imageUrl: Uri?,
        context: Context,
        handler: (Result<String>) -> Unit
    )


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
}