package com.takhaki.schoolfoodnavigator.Repository

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.takhaki.schoolfoodnavigator.Model.AssessmentEntity
import com.takhaki.schoolfoodnavigator.Model.CompanyData
import io.reactivex.Single

class AssesmentRepository(shopId: String, context: Context) {

    private val collectionRef: CollectionReference

    init {
        val id = CompanyData.getCompanyId(context)
        collectionRef = FirebaseFirestore.getInstance().collection("Team").document(id.toString())
            .collection("Shops").document(shopId)
            .collection("comment")
    }

    fun fetchAllAssesment(): Single<List<AssessmentEntity>> {
        return Single.create<List<AssessmentEntity>> { emitter ->
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

        // TODO: - アップロードする時にShopの最終更新日時を更新するようにする
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
            "comment" to assessment.comment
        )
    }
}