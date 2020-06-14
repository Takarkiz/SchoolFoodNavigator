package com.takhaki.schoolfoodnavigator.screen.detail.view_model

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.takhaki.schoolfoodnavigator.repository.AssessmentRepository
import com.takhaki.schoolfoodnavigator.repository.FirestorageRepository
import com.takhaki.schoolfoodnavigator.repository.ShopRepository
import com.takhaki.schoolfoodnavigator.repository.UserRepository
import com.takhaki.schoolfoodnavigator.screen.detail.DetailNavigatorAbstract
import com.takhaki.schoolfoodnavigator.screen.detail.DetailViewModelBase
import com.takhaki.schoolfoodnavigator.screen.detail.model.AboutShopDetailModel
import com.takhaki.schoolfoodnavigator.screen.detail.model.CommentDetailModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import java.lang.ref.WeakReference

class DetailViewModel(
    application: Application,
    private val shopId: String,
    private val navigator: DetailNavigatorAbstract
) : DetailViewModelBase(application) {

    // DetailViewModelContract

    override fun activity(activity: AppCompatActivity) {
        navigator.weakActivity = WeakReference(activity)
    }

    override val shopDetail: LiveData<AboutShopDetailModel>
        get() = _shopDetail
    override val scoreList: LiveData<List<CommentDetailModel>>
        get() = _scoreList
    override val hasCurrentUserComment: LiveData<Boolean>
        get() = _hasCurrentUserComment

    override fun didTapAddFab() {
        shopDetail.value?.let {
            navigator.toAssessmentView(shopId, it.name)
        }
    }

    override fun didTapDeleteShop(handler: (Result<String>) -> Unit) {
        val repository = ShopRepository(getApplication())
        val storage = FirestorageRepository()
        _shopDetail.value?.let {
            repository.deleteShop(it.id) { result ->
                it.imageUrl?.let { url ->
                    storage.deleteFile(url) { imageResult ->
                        handler(imageResult)
                    }
                } ?: run {
                    handler(result)
                }
            }
        }
    }

    // DetailAdapter.UserIconClickListener

    override fun onClickIcon(userId: String) {
        navigator.toUserProfile(userId)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    // LifecycleObserver

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        loadShopDetail()
    }

    // Private

    private val disposable: CompositeDisposable = CompositeDisposable()
    private val shopRepository = ShopRepository(getApplication())
    private val scores = mutableListOf<CommentDetailModel>()

    private val _shopDetail = MutableLiveData<AboutShopDetailModel>()
    private val _scoreList = MutableLiveData<List<CommentDetailModel>>()
    private val _hasCurrentUserComment = MutableLiveData<Boolean>().apply { value = false }


    private fun loadShopDetail() {
        val auth = UserRepository(getApplication())
        val repository = AssessmentRepository(shopId, getApplication())
        repository.fetchAllAssessment()
            .subscribeBy(
                onNext = { results ->
                    results.forEach { result ->
                        if (result.user == auth.currentUser?.uid) _hasCurrentUserComment.postValue(
                            true
                        )
                        auth.fetchUser(result.user)
                            .subscribeBy(
                                onNext = {
                                    val comment =
                                        CommentDetailModel(
                                            id = result.user,
                                            name = it.name,
                                            userIcon = it.iconUrl,
                                            gScore = result.good,
                                            dScore = result.distance,
                                            cScore = result.cheep,
                                            comment = result.comment,
                                            date = result.createdDate
                                        )
                                    scores.add(comment)
                                    _scoreList.postValue(scores)
                                },
                                onError = {
                                    Timber.d(it)
                                }
                            ).addTo(disposable)
                    }

                    val goodAverage = results.map { it.good }.average().toFloat()
                    val distanceAverage = results.map { it.distance }.average().toFloat()
                    val cheepAverage = results.map { it.cheep }.average().toFloat()
                    generateCommentModel(goodAverage, distanceAverage, cheepAverage)
                },
                onError = {
                    Timber.e(it)
                }).addTo(disposable)
    }

    private fun generateCommentModel(gAve: Float, dAve: Float, cAve: Float) {
        shopRepository.shop(shopId)
            .subscribe { shop ->
                val shopDetailModel =
                    AboutShopDetailModel(
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
            }.addTo(disposable)
    }


}