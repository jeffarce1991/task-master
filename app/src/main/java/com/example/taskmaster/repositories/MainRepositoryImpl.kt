package com.example.taskmaster.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.taskmaster.api.UsersApi
import com.example.taskmaster.models.User
import com.example.taskmaster.room.UsersDao
import com.example.taskmaster.utils.NetworkMapper
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.net.UnknownHostException
import javax.inject.Inject


/*
* Singleton pattern
* */
class MainRepositoryImpl
    @Inject
    constructor(
        private val usersDao: UsersDao,
        private val usersApi: UsersApi
    ) : MainRepository {

    private var networkMapper: NetworkMapper = NetworkMapper()

    private val handler = CoroutineExceptionHandler { _, exception ->
        println("debug: Exception thrown: $exception")
    }

    var job: CompletableJob? = null

    override fun getUsers(): MutableLiveData<MutableList<User>> {
        job = Job()
        return object : MutableLiveData<MutableList<User>>() {
            override fun onActive() {
                super.onActive()
                job?.let { theJob ->
                    CoroutineScope(IO + theJob).launch {
                        try {
                            val cachedUsers = usersDao.get()
                            println("debug: Cached Users $cachedUsers")
                            withContext(Main) {
                                value = cachedUsers
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

    override fun getByIdLive(id: Int): LiveData<User> {
        job = Job()
        return object: LiveData<User>(){
            override fun onActive() {
                super.onActive()
                job?.let{ theJob ->
                    CoroutineScope(IO + theJob).launch {
                        val user = usersDao.findById(id)
                        println("debug: User ${user.name}")
                        withContext(Main){
                            value = user
                            theJob.complete()
                        }
                    }
                }
            }
        }
    }

    override fun addTask(user: User): Long {
        val result = usersDao.insert(user)
        println("debug: addTask result $result")
        return result
    }

    override fun getById(id: Int): User {
        return usersDao.findById(id)
    }


    fun cancelJobs() {
        job?.cancel()
    }
}