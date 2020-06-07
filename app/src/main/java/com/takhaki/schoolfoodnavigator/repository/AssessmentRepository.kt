package com.takhaki.schoolfoodnavigator.repository

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.takhaki.schoolfoodnavigator.entity.AssessmentEntity
import com.takhaki.schoolfoodnavigator.entity.CompanyData
import io.reactivex.Single
import java.util.*

class AssessmentRepository(shopId: String, context: Context) {

    private val collectionRef: CollectionReference

    init {
        val id = CompanyData.getCompanyId(context)
        collectionRef = FirebaseFirestore.getInstance().collection("Team").document(id.toString())
            .collection("Shops").document(shopId)
            .collection("comment")
    }

    fun fetchAllAssessment(): Single<List<AssessmentEntity>> {
        return Single.create { emitter ->
            collectionRef.get()
                .addOnSuccessListener { snapshot ->
                    val assessments = snapshot.documents.mapNotNull {
                        val assessment = it.toObject(AssessmentEntity::class.java)
                        assessment?.toEntity()
                    }
                    emitter.onSuccess(assessments)
                }
                .addOnFailureListener {
                    emitter.tryOnError(it)
                }
        }
    }

    fun addAssessment(assessment: AssessmentEntity, handler: (Result<String>) -> Unit) {

        collectionRef
            .add(assesmentToMap(assessment))
            .addOnSuccessListener { doc ->
                handler(Result.success(doc.id))
            }.addOnFailureListener { e ->
                handler(Result.failure(e))
            }
    }

    private fun assesmentToMap(assessment: AssessmentEntity): Map<String, Any> {

        return mapOf(
            "user" to assessment.user,
            "good" to assessment.good,
            "distance" to assessment.distance,
            "cheep" to assessment.cheep,
            "comment" to assessment.comment,
            "createdDate" to assessment.createdDate
        )
    }
}