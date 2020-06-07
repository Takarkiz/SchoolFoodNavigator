package com.takhaki.schoolfoodnavigator.model

import android.content.Context
import androidx.core.content.edit

data class Company(
    val companyID: String,
    val name: String,
    val users: List<String>
)

class CompanyData {

    companion object {
        fun saveCompanyId(id: Int, context: Context) {
            val sharedPreference = context.getSharedPreferences("Company", Context.MODE_PRIVATE)
            sharedPreference.edit {
                putInt("ID", id)
                apply()
            }
        }

        // チームIDを返す(もしなければNullが返る)
        fun getCompanyId(context: Context): Int {
            val pref = context.getSharedPreferences("Company", Context.MODE_PRIVATE)
            return pref.getInt("ID", 0)
        }

        // 端末のチームIDを削除する
        fun deleteCompanyId(context: Context) {
            val pref = context.getSharedPreferences("Company", Context.MODE_PRIVATE)
            pref.edit {
                remove("ID")
                commit()
            }
        }
    }
}

