package com.example.storyappsubmission.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyappsubmission.data.remote.response.ErrorResponse
import com.example.storyappsubmission.data.remote.response.ListStoryItem
import com.example.storyappsubmission.domain.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val repository: StoryRepository
) : ViewModel(){
    private val _listStories = MutableLiveData<List<ListStoryItem>>()
    val listStories: LiveData<List<ListStoryItem>> = _listStories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _feedbackToast = MutableLiveData<String>()
    val feedbackToast: LiveData<String> = _feedbackToast

    init {
        getStoriesWithLocation()
    }

    private fun getStoriesWithLocation() {
        _isLoading.value = true
        repository.getStoriesWithLocation(onSuccess = { storyResponse ->
            Log.d(TAG, "onResponse: ${storyResponse.message}")
            _listStories.value = storyResponse.listStory!!
            _isLoading.value = false
        }, onError = { errorResponse ->
            showError(errorResponse)
            _isLoading.value = false
        })
    }

    private fun showError(errorResponse: ErrorResponse) {
        val errorMessage = errorResponse.message!!
        _feedbackToast.value = errorMessage

        Log.e(TAG, "onErrorResponse: ${errorResponse.message}")
    }

    companion object {
        private const val TAG = "MapsViewModel"
    }
}