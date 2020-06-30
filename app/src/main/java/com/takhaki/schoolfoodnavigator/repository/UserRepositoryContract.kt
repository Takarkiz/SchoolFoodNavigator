package com.takhaki.schoolfoodnavigator.repository

import com.google.firebase.auth.FirebaseUser
import com.takhaki.schoolfoodnavigator.entity.UserEntity
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

interface UserRepositoryContract {

    /**
     * 現在のユーザー情報
     */
    val currentUser: FirebaseUser?


    /**
     * 匿名サインイン
     */
    fun signInUser(): Single<String>


    /**
     * 新規ユーザーの登録
     *
     * @param name      ユーザー登録名
     * @param iconUrl   fire-storage保存先URL
     *
     */
    fun createUser(name: String, iconUrl: String?): Single<String>


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
    fun addFavoriteShop(id: String): Single<Unit>


    /**
     * お気に入りリストからショップを削除する
     *
     * @param id ショップID
     */
    fun removeFavoriteShop(id: String): Single<Unit>


    /**
     * ショップ追加のポイントを追加する
     */
    fun addPointShop()


    /**
     * コメント追加のポイントを追加する
     */
    fun addPointAssessment()


}