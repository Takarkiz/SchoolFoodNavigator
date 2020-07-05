package com.takhaki.schoolfoodnavigator.repository

import android.content.Context
import androidx.core.content.edit
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.takhaki.schoolfoodnavigator.entity.Company
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CompanyRepository(private val context: Context) : CompanyRepositoryContract {

    override val companyId: Int
        get() {
            val pref = context.getSharedPreferences("Company", Context.MODE_PRIVATE)
            return pref.getInt("ID", 0)
        }

    override val company: Flow<Result<Company>>
        get() = fetchCompany()

    override suspend fun joinTeam(userId: String): Unit = suspendCoroutine { con ->
        companyDB.document(companyId.toString())
            .update("members", FieldValue.arrayUnion(userId))
            .addOnSuccessListener {
                con.resume(Unit)
            }
            .addOnFailureListener {
                con.resumeWithException(it)
            }
    }

    override fun deleteCompanyIdCache() {
        val pref = context.getSharedPreferences("Company", Context.MODE_PRIVATE)
        pref.edit {
            remove("ID")
            commit()
        }
    }

    override suspend fun createCompanyRoom(name: String): Int = suspendCoroutine { con ->
        val teamID: Int = (1..999999).shuffled().first()
        companyDB.document(teamID.toString()).set(
            mapOf(
                "id" to teamID,
                "name" to name,
                "members" to listOf<String>()
            )
        ).addOnSuccessListener {
            con.resumeWith(Result.success(teamID))
        }.addOnFailureListener { e ->
            con.resumeWith(Result.failure(e))
        }
        return@suspendCoroutine
    }


    override suspend fun searchCompany(expectId: Int): Boolean = suspendCoroutine { cont ->
        companyDB.whereEqualTo("id", expectId).get()
            .addOnSuccessListener {
                // 存在すればtrueを返す
                cont.resumeWith(Result.success(!it.isEmpty))
            }
            .addOnFailureListener { e ->
                cont.resumeWith(Result.failure(e))
            }

    }


    override fun setCompanyId(id: Int) {
        val sharedPreference = context.getSharedPreferences("Company", Context.MODE_PRIVATE)
        sharedPreference.edit {
            putInt("ID", id)
            apply()
        }
    }

    private val companyDB = FirebaseFirestore.getInstance().collection("Team")

    @ExperimentalCoroutinesApi
    private fun fetchCompany(): Flow<Result<Company>> = callbackFlow {

        val task = companyDB.document(companyId.toString()).addSnapshotListener { snapshot, error ->

            if (error != null) {
                return@addSnapshotListener
            }

            if (snapshot == null) {
                return@addSnapshotListener
            }

            snapshot.toObject(Company::class.java)?.let { company ->
                offer(Result.success(company))

            }

        }

        awaitClose { task.remove() }
    }
}