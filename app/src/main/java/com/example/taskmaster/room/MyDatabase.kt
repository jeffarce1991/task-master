package com.example.taskmaster.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.taskmaster.models.Task
import com.example.taskmaster.room.converter.UserConverter

@Database(
    entities = [Task::class ],
    version = 3,
    exportSchema = false
)
@TypeConverters(
    //UserConverter::class
)
abstract class MyDatabase: RoomDatabase() {

    abstract fun userDao(): TaskDao

    companion object{
        const val DATABASE_NAME: String = "task_master_db"
    }
}