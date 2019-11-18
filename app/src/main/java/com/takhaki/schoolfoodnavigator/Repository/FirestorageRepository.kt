package com.takhaki.schoolfoodnavigator.Repository

import android.content.Context
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.takhaki.schoolfoodnavigator.Utility.getFileName
import java.io.File
import java.io.FileInputStream

class FirestorageRepository(private val filePathScheme: String) {

    val storageUrl = "gs://schoolfoodnavigator.appspot.com"
    val storage = FirebaseStorage.getInstance(storageUrl)

    /**
     * ユーザーアイコンをFirestorageにアップロードする
     * @param id       ユーザーを識別するID
     * @param iconUri   端末内の画像の参照を示すUri
     * @param handler   Firestorage上の画像URLを返す
     */
    fun uploadUserPhoto(
        id: String,
        iconUri: Uri,
        context: Context,
        handler: (Result<String>) -> Unit
    ) {

        val fileName = iconUri.getFileName(context) ?: ""
        val filePath = "${filePathScheme}/${id}/${fileName}"
        val shopImageRef = storage.reference.child(filePath)

        val stream = FileInputStream(File(iconUri.path!!))
        val uploadTask = shopImageRef.putStream(stream)
        uploadTask.addOnFailureListener { error ->
            handler(Result.failure(error))

        }.addOnSuccessListener {
            handler(Result.success(filePath))
        }
    }

    fun getGSReference(urlPath: String): StorageReference {
            return storage.getReferenceFromUrl("gs://schoolfoodnavigator.appspot.com/${urlPath}")
    }
}