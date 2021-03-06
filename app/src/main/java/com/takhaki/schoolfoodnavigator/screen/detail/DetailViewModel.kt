package com.takhaki.schoolfoodnavigator.screen.detail

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.takhaki.schoolfoodnavigator.repository.AssessmentRepository
import com.takhaki.schoolfoodnavigator.repository.FirestorageRepository
import com.takhaki.schoolfoodnavigator.repository.ShopInfoRepository
import com.takhaki.schoolfoodnavigator.repository.UserAuth
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
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
        val repository = ShopInfoRepository(getApplication())
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
    private val shopRepository = ShopInfoRepository(getApplication())
    private val scores = mutableListOf<CommentDetailModel>()

    private val _shopDetail = MutableLiveData<AboutShopDetailModel>()
    private val _scoreList = MutableLiveData<List<CommentDetailModel>>()
    private val _hasCurrentUserComment = MutableLiveData<Boolean>().apply { value = false }


    private fun loadShopDetail() {
        val auth = UserAuth(getApplication())
        val repository = AssessmentRepository(shopId, getApplication())
        repository.fetchAllAssessment()
            .subscribeBy(onSuccess = { results ->
                results.forEach { result ->
                    if (result.user == auth.currentUser?.uid) _hasCurrentUserComment.value = true
                    auth.userNameAndIconPath(result.user) {
                        it.getOrNull()?.let { pair ->
                            val comment = CommentDetailModel(
                                id = result.user,
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
        shopRepository.shop(shopId)
            .subscribe { shop ->
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
            }.addTo(disposable)
    }


}