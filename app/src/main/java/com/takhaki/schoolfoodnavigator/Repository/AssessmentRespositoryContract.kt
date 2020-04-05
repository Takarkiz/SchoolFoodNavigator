package com.takhaki.schoolfoodnavigator.Repository

import com.takhaki.schoolfoodnavigator.Model.AssessmentEntity
import io.reactivex.Single

/**
 * お店の評価 Repository
 */
interface AssessmentRespositoryContract {

    /**
     * 全ての評価を取得する
     */
    fun fetchAllAssessment(): Single<List<AssessmentEntity>>

    /**
     * 新たな評価の追加
     */
    fun addAssessment(assessment: AssessmentEntity, handler: (Result<String>) -> Unit)
}