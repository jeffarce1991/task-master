package com.example.taskmaster.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taskmaster.models.User

@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(blogEntity: User): Long

    @Query("SELECT * FROM users")
    suspend fun get(): MutableList<User>

    @Query("SELECT * FROM users WHERE id LIKE :id LIMIT 1")
    suspend fun findById(id: Int): User
}
