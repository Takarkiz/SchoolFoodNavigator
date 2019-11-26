package com.takhaki.schoolfoodnavigator.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.takhaki.schoolfoodnavigator.Repository.AssesmentRepository
import com.takhaki.schoolfoodnavigator.Repository.ShopInfoRepository
import com.takhaki.schoolfoodnavigator.Repository.UserAuth
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

    val scoreList: LiveData<List<CommentDetailModel>>
        get() = _scoreList
    private val _scoreList = MutableLiveData<List<CommentDetailModel>>()

    val isFavorite: LiveData<Boolean>
        get() = _isFavorite
    private val _isFavorite = MutableLiveData<Boolean>()
    private val scores = mutableListOf<CommentDetailModel>()

    fun putShopId(id: String) {
        shopId = id
    }

    fun didTapFavorite() {
        val auth = UserAuth()
        auth.addFavoriteShop(shopId) {
            Log.d("TAG", "成功")
        }
    }

    fun isFavorite() {
        val auth = UserAuth()
        auth.checkFavoriteShop(shopId) { fav ->
            _isFavorite.value = fav
        }
    }

    fun loadShopDetail() {
        val auth = UserAuth()
        val repository = AssesmentRepository(shopId)
        repository.fetchAllAssesment()
            .subscribeBy(onSuccess = { results ->
                results.forEach { result ->
                    auth.userNameAndIconPath(result.user) {
                        it.getOrNull()?.let { pair ->
                            val comment = CommentDetailModel(
                                name = pair.first,
                                userIcon = pair.second,
                                gScore = result.good,
                                dScore = result.distance,
                                cScore = result.cheep,
                                comment = result.comment
                            )

                            scores.add(comment)
                        }
                    }

                }

                _scoreList.value = scores
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
                onSuccess = { shop ->
                    val shopDetailModel = AboutShopDetailModel(
                        id = shop.id,
                        name = shop.name,
                        genre = shop.genre,
                        goodScore = gAve,
                        distance = dAve,
                        cheep = cAve,
                        score = (gAve + dAve + cAve) / 3,
                        imageUrl = if (shop.images.isNotEmpty()) shop.images[0] else null
                    )
                    _shopDetail.value = shopDetailModel
                }
            ).addTo(disposable)
    }


}