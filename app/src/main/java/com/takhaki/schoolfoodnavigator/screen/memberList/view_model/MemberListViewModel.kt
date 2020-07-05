package com.takhaki.schoolfoodnavigator.screen.memberList.view_model

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.takhaki.schoolfoodnavigator.entity.UserEntity
import com.takhaki.schoolfoodnavigator.repository.UserRepository
import com.takhaki.schoolfoodnavigator.screen.memberList.MemberListNavigatorAbstract
import com.takhaki.schoolfoodnavigator.screen.memberList.MemberListViewModelBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

    override fun didTapAddMember() {
        navigator.toAddMemberView()
    }

    // MemberListClickListener

    override fun onClickMember(userId: String) {
        navigator.toMemberProfile(userId)
    }


    // LifecycleObserver

    @ExperimentalCoroutinesApi
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        fetchUsers()
    }

    // Private

    private val _memberList: MutableLiveData<List<UserEntity>> = MutableLiveData()

    @ExperimentalCoroutinesApi
    private fun fetchUsers() {
        val auth = UserRepository(getApplication())
        viewModelScope.launch(Dispatchers.Main) {
            auth.fetchAllUser()
                .collectLatest { users ->
                    _memberList.postValue(users)
                }
        }
    }
}