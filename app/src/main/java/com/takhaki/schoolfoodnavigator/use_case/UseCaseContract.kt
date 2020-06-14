package com.takhaki.schoolfoodnavigator.use_case

import com.takhaki.schoolfoodnavigator.entity.Company
import com.takhaki.schoolfoodnavigator.entity.UserEntity
import io.reactivex.Flowable
import io.reactivex.Single

interface UseCaseContract {

    // Company

    val company: Flowable<Company>

    val companyId: Int

    fun setCompanyId(id: Int)

    fun deleteCompanyId()

    fun joinTeam(): Single<Unit>

    fun createCompanyRoom(name: String): Single<Int>

    fun searchCompany(expectId: Int): Single<Boolean>



    // User

    /**
     * 現在のユーザー
     */
    val currentUser: Flowable<UserEntity>

    /**
     * 全てのユーザー情報
     */
    val users: Flowable<List<UserEntity>>

    /**
     * 特定のIDのユーザー情報
     *
     * @param id ユーザーID
     */
    fun user(id: String): Flowable<UserEntity>

    /**
     * サインインする
     */
    fun signInUser(): Single<Unit>


}