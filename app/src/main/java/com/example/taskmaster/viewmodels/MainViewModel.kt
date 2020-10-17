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
        mUsers = mainRepository.getUsers()
    }


    val user: LiveData<User> = Transformations
        .switchMap(_userId){
            mainRepository.getByIdLive(it)
        }

    fun getUserById(userId: Int){
        if (_userId.value == userId) {
            return
        }
        _userId.value = userId
    }

    fun  addNewTask(user: User){
        mIsUpdating.postValue(true)
        job = Job()
        job?.let {
            CoroutineScope(Dispatchers.IO + it).launch(handler) {
                delay(1000)
                val userId = mainRepository.addTask(user)
                if (userId != -1L) {
                    val users: MutableList<User>? = mUsers!!.value
                    users!!.add(mainRepository.getById(userId.toInt()))
                    mUsers!!.postValue(users)
                }
                mIsUpdating.postValue(false)
                it.complete()
            }
        }
    }

    fun updateUser(user: User) {
        job = Job()
        job?.let {
            CoroutineScope(Dispatchers.IO + it).launch(handler) {
                mainRepository.addTask(user)
                mUser!!.postValue(user)
                it.complete()
            }
        }
    }

    fun getUsers() : MutableLiveData<MutableList<User>>? {
        return mUsers
    }

    fun getUser() : MutableLiveData<User>? {
        return mUser
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