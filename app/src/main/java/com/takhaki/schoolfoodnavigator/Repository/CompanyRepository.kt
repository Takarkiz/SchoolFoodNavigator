package com.takhaki.schoolfoodnavigator.Repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class CompanyRepository {

    val companyDB = FirebaseFirestore.getInstance().collection("Company")

    fun createCompany(teamName: String, handler: (Result<Int>) -> Unit) {

        val teamID: Int = (1..999999).shuffled().first()
        companyDB.document(teamID.toString()).set(
            mapOf(
                "id" to teamID,
                "name" to teamName,
                "members" to listOf<String>()
            )
        ).addOnSuccessListener {
            handler(Result.success(teamID))
        }.addOnFailureListener { e ->
            handler(Result.failure(e))
        }
    }

    fun joinMember(memberID: String, companyID: Int) {
        companyDB.document(companyID.toString()).update("members", FieldValue.arrayUnion(memberID))
    }
}