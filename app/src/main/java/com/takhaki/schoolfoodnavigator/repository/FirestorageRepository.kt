package com.takhaki.schoolfoodnavigator.repository

import android.content.Context
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.takhaki.schoolfoodnavigator.Model.CompanyData
import com.takhaki.schoolfoodnavigator.Utility.getFileName
import java.io.File
import java.io.FileInputStream

class FirestorageRepository {

    companion object {
        private val storageUrl = "gs://schoolfoodnavigator.appspot.com"
        private val storage = FirebaseStorage.getInstance(storageUrl)

        fun getGSReference(url: String): StorageReference {
            return if (url.contains("appspot.com")) {
                storage.getReferenceFromUrl(url)
            } else {
                // 前のバージョンの対応
                storage.getReferenceFromUrl("$storageUrl/$url")
            }
        }
    }

    /**
     * ユーザーアイコンをFirestorageにアップロードする
     * @param id       ユーザーを識別するID
     * @param imageUri   端末内の画像の参照を示すUri
     * @param handler   Firestorage上の画像URLを返す
     */
    fun uploadImage(
        id: String,
        imageUri: Uri,
        filePathScheme: StorageTypes,
        context: Context,
        handler: (Result<String>) -> Unit
    ) {

        val companyID = CompanyData.getCompanyId(context).toString()
        val fileName = imageUri.getFileName(context) ?: ""
        val filePath = "${companyID}/${filePathScheme.path}/${id}/${fileName}"
        val shopImageRef = storage.reference.child(filePath)

        val stream = FileInputStream(File(imageUri.path!!))
        val uploadTask = shopImageRef.putStream(stream)
        uploadTask.addOnFailureListener { error ->
            handler(Result.failure(error))
        }.addOnSuccessListener {
            val url = "$storageUrl/$filePath"
            handler(Result.success(url))
        }
    }
}


enum class StorageTypes(val path: String) {
    SHOP("Shops"),
    USER("User")
}