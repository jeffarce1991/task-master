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
                    CoroutineScope(IO + theJob).launch(handler) {

                        try {
                            val remoteUsers = usersApi.getUsers()
                            println("debug: Remote Users $remoteUsers")

                            for(dto in remoteUsers){
                                usersDao.insert(networkMapper.mapFromDto(dto))
                            }

                            withContext(Main) {
                                value = networkMapper.mapFromDtoList(remoteUsers) as MutableList<User>
                                theJob.complete()
                            }
                        } catch(e: Exception) {
                            if (e is UnknownHostException) {
                                val cachedUsers = usersDao.get()
                                println("debug: Cached Users $cachedUsers")
                                withContext(Main) {
                                    value = cachedUsers
                                    theJob.complete()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun getById(id: Int): LiveData<User> {
        job = Job()
        return object: LiveData<User>(){
            override fun onActive() {
                super.onActive()
                job?.let{ theJob ->
                    CoroutineScope(IO + theJob).launch {
                        val user = usersDao.findById(id)
                        println("debug: User ${user.company.name}")
                        withContext(Main){
                            value = user
                            theJob.complete()
                        }
                    }
                }
            }
        }
    }


    fun cancelJobs() {
        job?.cancel()
    }
}