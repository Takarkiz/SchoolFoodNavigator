package com.takhaki.schoolfoodnavigator.screen.assesment

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.takhaki.schoolfoodnavigator.Model.AssessmentEntity
import com.takhaki.schoolfoodnavigator.repository.AssessmentRepository
import com.takhaki.schoolfoodnavigator.repository.ShopInfoRepository
import com.takhaki.schoolfoodnavigator.repository.UserAuth
import java.lang.ref.WeakReference

class AssessmentViewModel(
    application: Application,
    private val shopId: String,
    private val navigator: AssessmentNavigatorAbstract
) : AssessmentViewModelBase(application) {

    override fun activity(activity: AppCompatActivity) {
        navigator.weakActivity = WeakReference(activity)
    }

    private val _goodValue = MutableLiveData<Float>().apply { value = 3f }
    override val goodValue: LiveData<Float>
        get() = _goodValue

    private val _distanceValue = MutableLiveData<Float>().apply { value = 3f }
    override val distanceValue: LiveData<Float>
        get() = _distanceValue

    private val _cheepValue = MutableLiveData<Float>().apply { value = 3f }
    override val cheepValue: LiveData<Float>
        get() = _cheepValue

    override val commentText = MutableLiveData<String>().apply { value = "" }

    override fun onUpdateGood(rating: Float) {
        _goodValue.value = rating
    }

    override fun onUpdateDistance(rating: Float) {
        _distanceValue.value = rating
    }

    override fun onUpdateCheep(rating: Float) {
        _cheepValue.value = rating
    }

    override fun uploadAssessment(finishUploadHandler: (Result<String>) -> Unit) {
        val auth = UserAuth(getApplication())
        val userId = auth.currentUser?.uid ?: return

        val good = goodValue.value ?: 3f
        val distance = distanceValue.value ?: 3f
        val cheep = cheepValue.value ?: 3f

        val repository = AssessmentRepository(shopId, getApplication())
        val assessment = AssessmentEntity(
            user = userId,
            good = good,
            distance = distance,
            cheep = cheep,
            comment = commentText.value ?: ""
        )

        val shopRepository = ShopInfoRepository(getApplication())

        repository.addAssessment(assessment) { result ->
            if (result.isSuccess) {
                try {
                    shopRepository.updateEditedDate(shopId)
                    auth.addPointAssessment()
                    finishUploadHandler(Result.success(result.getOrThrow()))
                } catch (e: Error) {
                    finishUploadHandler(Result.failure(e))
                }
            } else {
                result.exceptionOrNull()?.let { e ->
                    finishUploadHandler(Result.failure(e))
                }
            }
        }

    }

    override fun finishUpload() {
        navigator.backToHome()
    }

}