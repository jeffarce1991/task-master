package com.example.taskmaster.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.taskmaster.models.User

interface MainRepository {

    fun getUsers(): MutableLiveData<MutableList<User>>
    fun getById(id: Int): LiveData<User>
}