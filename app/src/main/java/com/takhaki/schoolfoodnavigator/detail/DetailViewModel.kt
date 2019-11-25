package com.takhaki.schoolfoodnavigator.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.takhaki.schoolfoodnavigator.Model.AssessmentEntity
import com.takhaki.schoolfoodnavigator.Repository.AssesmentRepository
import com.takhaki.schoolfoodnavigator.Repository.ShopInfoRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy

class DetailViewModel : ViewModel() {

    private val disposable: CompositeDisposable = CompositeDisposable()
    private val shopRepository = ShopInfoRepository()

    private var shopId: String = ""
    val shopDetail: LiveData<AboutShopDetailModel>
        get() = _shopDetail
    private val _shopDetail = MutableLiveData<AboutShopDetailModel>()

    val scoreList: LiveData<List<AssessmentEntity>>
        get() = _scoreList
    private val _scoreList = MutableLiveData<List<AssessmentEntity>>()

    fun putShopId(id: String) {
        shopId = id
    }

    fun loadShopDetail() {
        val repository = AssesmentRepository(shopId)
        repository.fetchAllAssesment()
            .subscribeBy(onSuccess = { results ->
                _scoreList.value = results
                val goodAverage = results.map { it.good }.average().toFloat()
                val distanceAverage = results.map { it.distance }.average().toFloat()
                val cheepAverage = results.map { it.cheep }.average().toFloat()
                createComment(goodAverage, distanceAverage, cheepAverage)
            },
                onError = {

                }).addTo(disposable)
    }

    private fun createComment(gAve: Float, dAve: Float, cAve: Float) {
        shopRepository.loadShop(shopId)
            .subscribeBy(
                onSuccess = { shop->
                    val shopDetailModel = AboutShopDetailModel(
                        id = shop.id,
                        name = shop.name,
                        genre = shop.genre,
                        goodScore = gAve,
                        distance = dAve,
                        cheep = cAve,
                        score = (gAve+dAve+cAve)/3,
                        imageUrl = if(shop.images.isNotEmpty()) shop.images[0] else null
                    )
                    _shopDetail.value = shopDetailModel
                }
            ).addTo(disposable)
    }


}