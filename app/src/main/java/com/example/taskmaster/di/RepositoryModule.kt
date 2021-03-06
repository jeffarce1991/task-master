package com.example.taskmaster.di

import com.example.taskmaster.api.UsersApi
import com.example.taskmaster.repositories.MainRepository
import com.example.taskmaster.repositories.MainRepositoryImpl
import com.example.taskmaster.room.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMainRepository(taskDao: TaskDao,
                              usersApi: UsersApi): MainRepository {
        return MainRepositoryImpl(
            taskDao,
            usersApi)
    }
}