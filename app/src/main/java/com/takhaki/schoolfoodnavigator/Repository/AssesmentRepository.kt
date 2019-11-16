package com.takhaki.schoolfoodnavigator.Repository

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.takhaki.schoolfoodnavigator.Model.AssessmentEntity

class AssesmentRepository(shopId: String) {

    private val assesmentDBRef: DocumentReference

    init {
        assesmentDBRef = FirebaseFirestore.getInstance().collection("Shops").document(shopId)
    }

    fun addAssessment(assessment: AssessmentEntity, handler: (Result<String>) -> Unit) {

        assesmentDBRef.set(assesmentToMap(assessment))
            .addOnSuccessListener {
                handler(Result.success("成功"))
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