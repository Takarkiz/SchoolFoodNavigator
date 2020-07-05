package com.takhaki.schoolfoodnavigator.repository

import com.google.firebase.auth.FirebaseUser
import com.takhaki.schoolfoodnavigator.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepositoryContract {

    /**
     * 現在のユーザー情報
     */
    val currentUser: FirebaseUser?


    /**
     * 匿名サインイン
     */
    suspend fun signInUser(): String


    /**
     * 新規ユーザーの登録
     *
     * @param name      ユーザー登録名
     * @param iconUrl   fire-storage保存先URL
     *
     */
    suspend fun createUser(name: String, iconUrl: String?)


    /**
     * 全てのユーザーの取得
     */
    fun fetchAllUser(): Flow<List<UserEntity>>


    /**
     * 特定のIDのユーザーを取得
     *
     * @param uid   ユーザーID
     */
    fun fetchUser(uid: String): Flow<UserEntity>


    /**
     * ショップをお気に入りリストに追加する
     *
     * @param id ショップID
     */
    suspend fun addFavoriteShop(id: String)


    /**
     * お気に入りリストからショップを削除する
     *
     * @param id ショップID
     */
    suspend fun removeFavoriteShop(id: String)


    /**
     * ショップ追加のポイントを追加する
     */
    fun addPointShop()


    /**
     * コメント追加のポイントを追加する
     */
    fun addPointAssessment()


}