package com.takhaki.schoolfoodnavigator.Repository

import android.content.Context
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.takhaki.schoolfoodnavigator.Model.CompanyData
import java.lang.Exception

class CompanyRepository(val context: Context) {

    private val companyDB = FirebaseFirestore.getInstance().collection("Team")

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

    fun searchCompany(expectedNumber: Int, handler: (Result<Boolean>) -> Unit) {
        companyDB.whereEqualTo("id", expectedNumber).get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    handler(Result.success(false))
                } else {
                    handler(Result.success(true))
                }
            }
            .addOnFailureListener { e ->
                handler(Result.failure(e))
            }
    }

    fun fetchCompanyName(handler: (Result<String>) -> Unit) {
        val teamID = CompanyData.getCompanyId(context)
        companyDB.document(teamID.toString()).get()
            .addOnSuccessListener { data ->
                val name = data["name"].toString()
                handler(Result.success(name))
            }.addOnFailureListener { e ->
                handler(Result.failure(e))
            }
    }
}