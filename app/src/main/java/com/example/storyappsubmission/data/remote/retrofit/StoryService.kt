package com.example.storyappsubmission.data.remote.retrofit

import com.example.storyappsubmission.data.remote.response.AuthResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface StoryService {
    @GET("stories")
    fun getAllStories(
        @Header("Authorization") token: String
    ): Call<AuthResponse>

    @GET("stories/{id}")
    fun getStoryDetailById(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Call<AuthResponse>
}