package com.takhaki.schoolfoodnavigator.use_case

import com.takhaki.schoolfoodnavigator.entity.Company
import com.takhaki.schoolfoodnavigator.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UseCaseContract {

    // Company

    val company: Flow<Company>

    val companyId: Int

    fun setCompanyId(id: Int)

    fun deleteCompanyId()

    fun joinTeam()

    fun createCompanyRoom(name: String): Int

    fun searchCompany(expectId: Int): Boolean



    // User

    /**
     * 現在のユーザー
     */
    val currentUser: Flow<UserEntity>

    /**
     * 全てのユーザー情報
     */
    val users: Flow<List<UserEntity>>

    /**
     * 特定のIDのユーザー情報
     *
     * @param id ユーザーID
     */
    fun user(id: String): Flow<UserEntity>

    /**
     * サインインする
     */
    fun signInUser()


}