package com.example.storyappsubmission.data.repository

import com.example.storyappsubmission.data.remote.response.ErrorResponse
import com.example.storyappsubmission.data.remote.response.ListStoryResponse
import com.example.storyappsubmission.data.remote.retrofit.StoryService
import com.example.storyappsubmission.domain.repository.StoryRepository
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepositoryImpl(private val storyService: StoryService) : StoryRepository {
    override fun getAllStories(
        onSuccess: (ListStoryResponse) -> Unit,
        onError: (ErrorResponse) -> Unit
    ) {
        val client = storyService.getAllStories()

        client.enqueue(object : Callback<ListStoryResponse> {
            override fun onResponse(
                call: Call<ListStoryResponse>,
                response: Response<ListStoryResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        onSuccess(responseBody)
                    }
                } else {
                    val responseError = response.errorBody()
                    responseError?.let {
                        val errorResponse = Gson().fromJson(it.string(), ErrorResponse::class.java)
                        onError(errorResponse)
                    }
                }
            }

            override fun onFailure(call: Call<ListStoryResponse>, t: Throwable) {
                val errorResponse = ErrorResponse(message = t.message)
                onError(errorResponse)
            }
        })
    }

    override fun getStoryById(
        id: String,
        onSuccess: (ListStoryResponse) -> Unit,
        onError: (ErrorResponse) -> Unit
    ) {
        val client = storyService.getStoryDetailById(id)

        client.enqueue(object : Callback<ListStoryResponse> {
            override fun onResponse(
                call: Call<ListStoryResponse>,
                response: Response<ListStoryResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        onSuccess(responseBody)
                    }
                } else {
                    val responseError = response.errorBody()
                    responseError?.let {
                        val errorResponse = Gson().fromJson(it.string(), ErrorResponse::class.java)
                        onError(errorResponse)
                    }
                }
            }

            override fun onFailure(call: Call<ListStoryResponse>, t: Throwable) {
                val errorResponse = ErrorResponse(message = t.message)
                onError(errorResponse)
            }
        })
    }

    override fun addNewStory(
        file: MultipartBody.Part,
        description: RequestBody,
        onSuccess: (ListStoryResponse) -> Unit,
        onError: (ErrorResponse) -> Unit
    ) {
        val client = storyService.addNewStory(file, description)

        client.enqueue(object : Callback<ListStoryResponse> {
            override fun onResponse(
                call: Call<ListStoryResponse>,
                response: Response<ListStoryResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        onSuccess(responseBody)
                    }
                } else {
                    val responseError = response.errorBody()
                    responseError?.let {
                        val errorResponse = Gson().fromJson(it.string(), ErrorResponse::class.java)
                        onError(errorResponse)
                    }
                }
            }

            override fun onFailure(call: Call<ListStoryResponse>, t: Throwable) {
                val errorResponse = ErrorResponse(message = t.message)
                onError(errorResponse)
            }
        })
    }

}