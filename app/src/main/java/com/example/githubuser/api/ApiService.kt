package com.example.githubuser.api

import com.example.githubuser.data.DetailUserResponse
import com.example.githubuser.data.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/search/users")
    fun getUser(
        @Query("q") query: String
    ): Call<SearchResponse>

    @GET("users/{username}")
    fun getUserByName(
        @Path("username") username: String
    ): Call<DetailUserResponse>
}