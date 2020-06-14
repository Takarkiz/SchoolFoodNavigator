package com.takhaki.schoolfoodnavigator.repository

import com.takhaki.schoolfoodnavigator.entity.AssessmentEntity
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * お店の評価 Repository
 */
interface AssessmentRepositoryContract {

    /**
     * 全ての評価を取得する
     */
    fun fetchAllAssessment(): Flowable<List<AssessmentEntity>>

    /**
     * 新たな評価の追加
     */
    fun addAssessment(assessment: AssessmentEntity): Single<Unit>
}