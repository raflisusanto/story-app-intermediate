package com.example.storyappsubmission.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.storyappsubmission.data.remote.response.ErrorResponse
import com.example.storyappsubmission.data.remote.response.ListStoryItem
import com.example.storyappsubmission.data.remote.response.ListStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface StoryRepository {
    fun getAllStories(
        onSuccess: (ListStoryResponse) -> Unit,
        onError: (ErrorResponse) -> Unit
    )

    fun addNewStory(
        file: MultipartBody.Part,
        description: RequestBody,
        onSuccess: (ListStoryResponse) -> Unit,
        onError: (ErrorResponse) -> Unit
    )

    fun getStoriesWithLocation(
        onSuccess: (ListStoryResponse) -> Unit,
        onError: (ErrorResponse) -> Unit
    )

    fun getAllPagedStories(): LiveData<PagingData<ListStoryItem>>
}