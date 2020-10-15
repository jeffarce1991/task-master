package com.example.taskmaster.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.taskmaster.models.User
import com.example.taskmaster.room.converter.UserConverter

@Database(
    entities = [User::class ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    UserConverter::class
)
abstract class MyDatabase: RoomDatabase() {

    abstract fun userDao(): UsersDao

    companion object{
        const val DATABASE_NAME: String = "mvvm_db"
    }
}