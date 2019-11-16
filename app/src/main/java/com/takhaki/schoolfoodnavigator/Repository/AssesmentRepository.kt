package com.takhaki.schoolfoodnavigator.Repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.takhaki.schoolfoodnavigator.Model.AssessmentEntity

class AssesmentRepository(shopId: String) {

    private val collectionRef: CollectionReference

    init {
        collectionRef = FirebaseFirestore.getInstance().collection("Shops").document(shopId)
            .collection("comment")
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
            "good" to assessment.good,
            "distance" to assessment.distance,
            "cheep" to assessment.cheep,
            "comment" to assessment.comment
        )
    }
}