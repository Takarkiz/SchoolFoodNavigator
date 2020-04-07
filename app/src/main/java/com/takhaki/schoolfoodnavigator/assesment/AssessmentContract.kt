package com.takhaki.schoolfoodnavigator.assesment

import android.app.Application
import androidx.lifecycle.*
import com.takhaki.schoolfoodnavigator.Repository.AssessmentRespositoryContract

abstract class AssessmentViewModelBase(
    application: Application
)  : AndroidViewModel(application),
        LifecycleObserver,
        AssessmentViewModelContract

interface AssessmentViewModelContract {

    val goodValue: LiveData<Float>

    val distanceValue: LiveData<Float>

    val cheepValue: LiveData<Float>

    val commentText: MutableLiveData<String>

    fun onUpdateGood(rating: Float)

    fun onUpdateDistance(rating: Float)

    fun onUpdateCheep(rating: Float)

    fun uploadAssessment(finishUploadHandler: (Result<String>) -> Unit)
}

abstract class