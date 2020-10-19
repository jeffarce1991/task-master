package com.example.taskmaster.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taskmaster.models.Task

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task): Long

    @Query("SELECT * FROM tasks")
    suspend fun get(): MutableList<Task>

    @Query("SELECT * FROM tasks WHERE id LIKE :id LIMIT 1")
    fun findById(id: Int): Task

    @Query("SELECT * FROM tasks WHERE status LIKE :status")
    fun findByStatus(status: Int): MutableList<Task>
}
