package com.example.taskmaster.di

import android.content.Context
import androidx.room.Room
import com.example.taskmaster.room.MyDatabase
import com.example.taskmaster.room.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object RoomModule {

    @Singleton
    @Provides
    fun provideDB(@ApplicationContext context: Context): MyDatabase{
        return Room
            .databaseBuilder(
                context,
                MyDatabase::class.java,
                MyDatabase.DATABASE_NAME
            ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideUsersDao(myDatabase: MyDatabase): TaskDao {
        return myDatabase.userDao()
    }

}