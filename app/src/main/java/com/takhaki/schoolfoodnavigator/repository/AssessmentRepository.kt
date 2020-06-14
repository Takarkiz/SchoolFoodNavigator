package com.takhaki.schoolfoodnavigator.repository

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.takhaki.schoolfoodnavigator.entity.AssessmentEntity
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

class AssessmentRepository(shopId: String, context: Context) : AssessmentRepositoryContract {

    private val collectionRef: CollectionReference

    init {
        val companyRepository = CompanyRepository(context)
        val id = companyRepository.companyId
        collectionRef = FirebaseFirestore.getInstance().collection("Team").document(id.toString())
            .collection("Shops").document(shopId)
            .collection("comment")
    }

    override fun fetchAllAssessment(): Flowable<List<AssessmentEntity>> {
        return Flowable.create({ emitter ->
            collectionRef.addSnapshotListener { snapshot, error ->

                if (error != null) {
                    emitter.onError(error)
                    return@addSnapshotListener
                }

                if (snapshot == null) {
                    return@addSnapshotListener
                }

                val assessments = snapshot.documents.mapNotNull {
                    val assessment = it.toObject(AssessmentEntity::class.java)
                    assessment?.toEntity()
                }
                emitter.onNext(assessments)
            }
        }, BackpressureStrategy.BUFFER)
    }

    override fun addAssessment(assessment: AssessmentEntity): Single<Unit> {

        return Single.create { emitter ->
            collectionRef
                .add(assesmentToMap(assessment))
                .addOnSuccessListener { doc ->
                    emitter.onSuccess(Unit)
                }.addOnFailureListener { e ->
                    emitter.onError(e)
                }
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