package com.takhaki.schoolfoodnavigator.assesment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.takhaki.schoolfoodnavigator.Model.AssessmentEntity
import com.takhaki.schoolfoodnavigator.Repository.AssesmentRepository
import com.takhaki.schoolfoodnavigator.Repository.UserAuth

class AssesmentViewModel : ViewModel() {

    private val _shopId = MutableLiveData<String>()
    val shopId: LiveData<String>
        get() = _shopId

    private val _goodValue = MutableLiveData<Float>().apply { value = 3f }
    val goodValue: LiveData<Float>
        get() = _goodValue

    private val _distanceValue = MutableLiveData<Float>().apply { value = 3f }
    val distanceValue: LiveData<Float>
        get() = _distanceValue

    private val _cheepValue = MutableLiveData<Float>().apply { value = 3f }
    val cheepValue: LiveData<Float>
        get() = _cheepValue

    val commentText = MutableLiveData<String>().apply { value = "" }


    fun putShopId(shopId: String) {
        _shopId.value = shopId
    }

    fun onUpdateGood(rating: Float) {
        _goodValue.value = rating
    }

    fun onUpdateDistance(rating: Float) {
        _distanceValue.value = rating
    }

    fun onUpdateCheep(rating: Float) {
        _cheepValue.value = rating
    }

    fun uploadAssessment(finishUploadHander: (Result<String>) -> Unit) {
        val userId = UserAuth().currentUser?.uid?.let { it } ?: return

        shopId.value?.let { id ->
            val good = goodValue.value?.let { it } ?: 3f
            val distance = distanceValue.value?.let { it } ?: 3f
            val cheep = cheepValue.value?.let { it } ?: 3f

            val repository = AssesmentRepository(id)
            val assesment = AssessmentEntity(
                userId = userId,
                good = good,
                distance = distance,
                cheep = cheep,
                totalScore = (good+distance+cheep)/3,
                comment = commentText.value?.let { it } ?: ""
            )

            repository.addAssessment(assesment) { result ->
                if (result.isSuccess) {
                    try {
                        finishUploadHander(Result.success(result.getOrThrow()))
                    } catch (e: Error) {
                        finishUploadHander(Result.failure(e))
                    }
                } else {
                    result.exceptionOrNull()?.let { e ->
                        finishUploadHander(Result.failure(e))
                    }
                }
            }
        }
    }

}