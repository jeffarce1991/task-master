package com.example.taskmaster.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.taskmaster.models.Task
import com.example.taskmaster.repositories.MainRepositoryImpl
import kotlinx.coroutines.*


class MainViewModel
@ViewModelInject
constructor(
    private val mainRepository: MainRepositoryImpl
): ViewModel(){

    private val _taskId: MutableLiveData<Int> = MutableLiveData()

    var job: CompletableJob? = null

    private val handler = CoroutineExceptionHandler { _, exception ->
        println("Exception thrown: $exception")
    }

    private var mTasks: MutableLiveData<MutableList<Task>>? = null
    private var mTask: MutableLiveData<Task>? = null
    private var mIsUpdating: MutableLiveData<Boolean> = MutableLiveData()
    init {
        mIsUpdating.postValue(true)
        mTasks = mainRepository.getTasks()
    }


    val task: LiveData<Task> = Transformations
        .switchMap(_taskId){
            mainRepository.getByIdLive(it)
        }

    fun getTaskById(id: Int){
        if (_taskId.value == id) {
            return
        }
        _taskId.value = id
    }

    fun  addNewTask(task: Task){
        mIsUpdating.postValue(true)
        job = Job()
        job?.let {
            CoroutineScope(Dispatchers.IO + it).launch(handler) {
                delay(1000)
                val id = mainRepository.addTask(task)
                if (id != -1L) {
                    val tasks: MutableList<Task>? = mTasks!!.value
                    tasks!!.add(mainRepository.getById(id.toInt()))
                    mTasks!!.postValue(tasks)
                }
                mIsUpdating.postValue(false)
                it.complete()
            }
        }
    }

    fun updateTask(task: Task) {
        job = Job()
        job?.let {
            CoroutineScope(Dispatchers.IO + it).launch(handler) {
                mainRepository.addTask(task)
                mTask!!.postValue(task)
                it.complete()
            }
        }
    }

    fun getTasks() : MutableLiveData<MutableList<Task>>? {
        return mTasks
    }

    fun getTask() : MutableLiveData<Task>? {
        return mTask
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