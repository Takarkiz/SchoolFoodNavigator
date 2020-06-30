package com.takhaki.schoolfoodnavigator.repository

import com.takhaki.schoolfoodnavigator.entity.Company
import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

interface CompanyRepositoryContract {

    /**
     * チームID
     */
    val companyId: Int

    /**
     * チーム情報
     */
    val company: Flow<Result<Company>>

    /**
     * 端末にチームIDを登録する
     *
     * @param id チームID
     */
    fun setCompanyId(id: Int)

    /**
     * チームIDを端末から削除する
     */
    fun deleteCompanyIdCache()

    /**
     * チームを作成する
     *
     * @param name  チーム名
     */
    fun createCompanyRoom(name: String): Single<Int>

    /**
     * メンバーを追加する
     *
     * @param userId メンバーID
     */
    fun joinTeam(userId: String): Single<Unit>

    /**
     * IDに対してチームが存在するかどうかを調べる
     *
     * @param expectId 調べるチームID
     */
    fun searchCompany(expectId: Int): Single<Boolean>

}