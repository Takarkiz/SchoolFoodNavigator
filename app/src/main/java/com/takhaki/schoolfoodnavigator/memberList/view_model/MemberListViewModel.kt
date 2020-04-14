package com.takhaki.schoolfoodnavigator.memberList.view_model

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.takhaki.schoolfoodnavigator.Model.UserEntity
import com.takhaki.schoolfoodnavigator.Repository.UserAuth
import com.takhaki.schoolfoodnavigator.memberList.MemberListNavigatorAbstract
import com.takhaki.schoolfoodnavigator.memberList.MemberListViewModelBase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import java.lang.ref.WeakReference

class MemberListViewModel(
    application: Application,
    private val navigator: MemberListNavigatorAbstract
) : MemberListViewModelBase(application) {

    // MemberListViewModelContract

    override val memberList: LiveData<List<UserEntity>>
        get() = _memberList

    override fun activity(activity: AppCompatActivity) {
        navigator.weakActivity = WeakReference(activity)
    }

    // MemberListClickListener

    override fun onClickMember(userId: String) {
        navigator.toMemberProfile(userId)
    }

    // ViewModel
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


    // LifecycleObserver

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        fetchUsers()
    }

    // Private

    private val _memberList: MutableLiveData<List<UserEntity>> = MutableLiveData()
    private val disposable: CompositeDisposable = CompositeDisposable()

    private fun fetchUsers() {
        val auth = UserAuth(getApplication())
        auth.fetchAllUser()
            .subscribeBy(
                onSuccess = { result ->
                    _memberList.postValue(result)
                },
                onError = {

                }).addTo(disposable)
    }
}