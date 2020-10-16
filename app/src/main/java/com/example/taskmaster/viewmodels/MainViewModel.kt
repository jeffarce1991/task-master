package com.example.taskmaster.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.taskmaster.models.User
import com.example.taskmaster.repositories.MainRepositoryImpl
import kotlinx.coroutines.*


class MainViewModel
@ViewModelInject
constructor(
    private val mainRepository: MainRepositoryImpl
): ViewModel(){

    private val _userId: MutableLiveData<Int> = MutableLiveData()

    var job: CompletableJob? = null

    private val handler = CoroutineExceptionHandler { _, exception ->
        println("Exception thrown: $exception")
    }

    private var mUsers: MutableLiveData<MutableList<User>>? = null
    private var mUser: MutableLiveData<User>? = null
    private var mIsUpdating: MutableLiveData<Boolean> = MutableLiveData()
    init {
        mIsUpdating.postValue(true)
        mUsers = mainRepository.getLiveUsers()
    }


    val user: LiveData<User> = Transformations
        .switchMap(_userId){
            mainRepository.getById(it)
        }

    fun getUserById(userId: Int){
        if (_userId.value == userId) {
            return
        }
        _userId.value = userId
    }

    fun  addNewValue(user: User) {
        mIsUpdating.postValue(true)
        job = Job()
        job?.let {
            CoroutineScope(Dispatchers.IO + it).launch(handler) {
                delay(1000)
                val users: MutableList<User>? = mUsers!!.value
                users!!.add(user)
                mUsers!!.postValue(users)
                mIsUpdating.postValue(false)
                it.complete()
            }
        }

    }

    fun getUsers() : MutableLiveData<MutableList<User>>? {
        return mUsers
    }


    fun getIsUpdating(): LiveData<Boolean?>? {
        return mIsUpdating
    }

    fun setIsUpdating(isUpdating: Boolean) {
        mIsUpdating.postValue(isUpdating)
    }

    fun cancelJobs() {
        mainRepository.cancelJobs()
    }
}