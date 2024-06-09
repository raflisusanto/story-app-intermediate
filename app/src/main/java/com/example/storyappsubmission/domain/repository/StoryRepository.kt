package com.example.storyappsubmission.domain.repository

import com.example.storyappsubmission.data.remote.response.ErrorResponse
import com.example.storyappsubmission.data.remote.response.ListStoryResponse

interface StoryRepository {
    fun getAllStories(
        onSuccess: (ListStoryResponse) -> Unit,
        onError: (ErrorResponse) -> Unit
    )

    fun getStoryById(
        id: String, onSuccess: (ListStoryResponse) -> Unit,
        onError: (ErrorResponse) -> Unit
    )
}