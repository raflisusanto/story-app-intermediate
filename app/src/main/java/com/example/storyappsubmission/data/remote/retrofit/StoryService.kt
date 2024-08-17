package com.example.storyappsubmission.data.remote.retrofit

import com.example.storyappsubmission.data.remote.response.ListStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface StoryService {
    @GET("stories")
    fun getAllStories(): Call<ListStoryResponse>

    @GET("stories")
    suspend fun getAllPagedStories(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): ListStoryResponse

    @Multipart
    @POST("stories")
    fun addNewStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<ListStoryResponse>

    @GET("stories")
    fun getStoriesWithLocation(
        @Query("location") location: Int = 1,
    ): Call<ListStoryResponse>
}