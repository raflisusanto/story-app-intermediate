package com.example.storyappsubmission.data.remote.retrofit

import com.example.storyappsubmission.data.remote.response.AuthResponse
import com.example.storyappsubmission.data.remote.response.ListStoryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface StoryService {
    @GET("stories")
    fun getAllStories(): Call<ListStoryResponse>

    @GET("stories/{id}")
    fun getStoryDetailById(
        @Path("id") id: String,
    ): Call<ListStoryResponse>
}