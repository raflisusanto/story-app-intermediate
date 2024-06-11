package com.example.storyappsubmission.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyappsubmission.data.remote.TokenManager
import com.example.storyappsubmission.data.remote.response.ErrorResponse
import com.example.storyappsubmission.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val pref: TokenManager,
    private val repository: AuthRepository
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorToast = MutableLiveData<String>()
    val errorToast: LiveData<String> = _errorToast

    private val _token = MutableLiveData<String>()

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        _isLoading.value = true
        repository.login(email, password, onSuccess = { authResponse ->
            Log.d(TAG, "onResponse: ${authResponse.message}")

            _token.value = authResponse.loginResult!!.token
            onSuccess()
            saveToken()

            _isLoading.value = false
        }, onError = { errorResponse ->
            showError(errorResponse)
            _isLoading.value = false
        })
    }

    fun register(name: String, email: String, password: String, onSuccess: () -> Unit) {
        _isLoading.value = true

        repository.register(name, email, password, onSuccess = { authResponse ->
            Log.d(TAG, "onResponse: ${authResponse.message}")
            onSuccess()
            _isLoading.value = false
        }, onError = { errorResponse ->
            showError(errorResponse)
            _isLoading.value = false
        })
    }

    private fun saveToken() {
        Log.d("AuthViewModel", "saveToken: ${_token.value}")
        runBlocking { pref.saveToken(_token.value!!) }
    }

    fun getToken(onTokenExist: () -> Unit, onTokenNotExist: () -> Unit) {
        runBlocking {
            _token.value = pref.getToken().first()
            if (_token.value.isNullOrEmpty()) {
                onTokenNotExist()
            } else {
                onTokenExist()
            }
        }
    }

    fun removeToken(onTokenRemoved: () -> Unit) {
        runBlocking {
            pref.removeToken()
            _token.value = ""
            onTokenRemoved()
        }
    }

    private fun showError(errorResponse: ErrorResponse) {
        val errorMessage = errorResponse.message!!
        _errorToast.value = errorMessage

        Log.e(TAG, "onErrorResponse: ${errorResponse.message}")
    }

    companion object {
        private const val TAG = "AuthViewModel"
    }
}