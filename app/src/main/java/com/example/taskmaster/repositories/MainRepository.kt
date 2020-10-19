package com.example.taskmaster.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.taskmaster.models.Task

interface MainRepository {

    fun getTasks(): MutableLiveData<MutableList<Task>>
    fun getByIdLive(id: Int): LiveData<Task>
    fun getById(id: Int): Task
    fun addTask(task: Task): Long
}