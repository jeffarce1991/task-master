package com.example.taskmaster.api

import com.example.taskmaster.models.UserDto
import retrofit2.http.GET
import retrofit2.http.Path

interface UsersApi {

    @GET("placeholder/user/{userId}")
    suspend fun getUser(
        @Path("userId") userId: String
    ): UserDto

    @GET("users")
    suspend fun getUsers(): MutableList<UserDto>
}