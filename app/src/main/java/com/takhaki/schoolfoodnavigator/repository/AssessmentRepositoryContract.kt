package com.takhaki.schoolfoodnavigator.repository

import com.takhaki.schoolfoodnavigator.entity.AssessmentEntity
import kotlinx.coroutines.flow.Flow

/**
 * お店の評価 Repository
 */
interface AssessmentRepositoryContract {

    /**
     * 全ての評価を取得する
     */
    suspend fun fetchAllAssessment(): Flow<List<AssessmentEntity>>

    /**
     * 新たな評価の追加
     */
    suspend fun addAssessment(assessment: AssessmentEntity)
}