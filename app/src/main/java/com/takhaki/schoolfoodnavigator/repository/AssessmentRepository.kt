package com.takhaki.schoolfoodnavigator.repository

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.takhaki.schoolfoodnavigator.entity.AssessmentEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AssessmentRepository(shopId: String, context: Context) : AssessmentRepositoryContract {

    private val collectionRef: CollectionReference

    init {
        val companyRepository = CompanyRepository(context)
        val id = companyRepository.companyId
        collectionRef = FirebaseFirestore.getInstance().collection("Team").document(id.toString())
            .collection("Shops").document(shopId)
            .collection("comment")
    }

    @ExperimentalCoroutinesApi
    override suspend fun fetchAllAssessment(): Flow<List<AssessmentEntity>> = callbackFlow {

        val task = collectionRef.addSnapshotListener { snapshot, error ->

            if (error != null) {
                return@addSnapshotListener
            }

            if (snapshot == null) {
                return@addSnapshotListener
            }

            val assessments = snapshot.documents.mapNotNull {
                val assessment = it.toObject(AssessmentEntity::class.java)
                assessment?.toEntity()
            }
            offer(assessments)
        }

        awaitClose { task.remove() }
    }

    override suspend fun addAssessment(assessment: AssessmentEntity): Unit = suspendCoroutine { con ->
        collectionRef
            .add(assesmentToMap(assessment))
            .addOnSuccessListener { doc ->
                con.resume(Unit)
            }.addOnFailureListener { e ->
                con.resumeWithException(e)
            }

    }

    private fun assesmentToMap(assessment: AssessmentEntity): Map<String, Any> {

        val date = Calendar.getInstance().run {
            add(Calendar.MONTH, -3)
            assessment.createdDate ?: time
        }

        return mapOf(
            "user" to assessment.user,
            "good" to assessment.good,
            "distance" to assessment.distance,
            "cheep" to assessment.cheep,
            "comment" to assessment.comment,
            "createdDate" to date
        )
    }
}