package com.example.taskmaster.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.taskmaster.api.UsersApi
import com.example.taskmaster.models.Task
import com.example.taskmaster.room.TaskDao
import com.example.taskmaster.utils.NetworkMapper
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Inject


/*
* Singleton pattern
* */
class MainRepositoryImpl
    @Inject
    constructor(
        private val taskDao: TaskDao,
        private val usersApi: UsersApi
    ) : MainRepository {

    private var networkMapper: NetworkMapper = NetworkMapper()

    private val handler = CoroutineExceptionHandler { _, exception ->
        println("debug: Exception thrown: $exception")
    }

    var job: CompletableJob? = null

    override fun getTasks(): MutableLiveData<MutableList<Task>> {
        job = Job()
        return object : MutableLiveData<MutableList<Task>>() {
            override fun onActive() {
                super.onActive()
                job?.let { theJob ->
                    CoroutineScope(IO + theJob).launch {
                        try {
                            val cachedTasks = taskDao.get()
                            println("debug: Cached Users $cachedTasks")
                            withContext(Main) {
                                value = cachedTasks
                                theJob.complete()
                            }
                        } catch(e: Exception) {
                            println("debug: Exception thrown: $e")
                        }
                    }
                }
            }
        }
    }

    override fun getByIdLive(id: Int): LiveData<Task> {
        job = Job()
        return object: LiveData<Task>(){
            override fun onActive() {
                super.onActive()
                job?.let{ theJob ->
                    CoroutineScope(IO + theJob).launch {
                        val task = taskDao.findById(id)
                        println("debug: Task ${task.title}")
                        withContext(Main){
                            value = task
                            theJob.complete()
                        }
                    }
                }
            }
        }
    }

    override fun addTask(task: Task): Long {
        val result = taskDao.insert(task)
        println("debug: addTask result $result")
        return result
    }

    override fun getById(id: Int): Task {
        return taskDao.findById(id)
    }


    fun cancelJobs() {
        job?.cancel()
    }
}