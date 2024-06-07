package com.example.storyappsubmission.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyappsubmission.data.TokenManager
import com.example.storyappsubmission.data.request.AuthRequest
import com.example.storyappsubmission.data.response.AuthResponse
import com.example.storyappsubmission.data.retrofit.ApiConfig
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(private val pref: TokenManager) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorToast = MutableLiveData<String>()
    val errorToast: LiveData<String> = _errorToast

    private val _token = MutableLiveData<String>()

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        _isLoading.value = true

        val loginRequest = AuthRequest(email = email, password = password)
        val client = ApiConfig.getApiService().login(loginRequest)

        client.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(
                call: Call<AuthResponse>,
                response: Response<AuthResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val token = responseBody.loginResult!!.token
                        _token.value = token

                        saveToken()
                        onSuccess()

                        Log.d(TAG, "onResponse: ${responseBody.message}")
                    }
                } else {
                    val responseError = response.errorBody()
                    responseError?.let {
                        val errorResponse = Gson().fromJson(it.string(), AuthResponse::class.java)
                        _errorToast.value = errorResponse.message
                        Log.e(TAG, "onFailureResponse: ${errorResponse.message}")
                    }
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                _errorToast.value = t.message
            }
        })
    }

    fun register(name: String, email: String, password: String, onSuccess: () -> Unit) {
        _isLoading.value = true

        val registerRequest = AuthRequest(email = email, password = password, name = name)
        val client = ApiConfig.getApiService().register(registerRequest)

        client.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(
                call: Call<AuthResponse>,
                response: Response<AuthResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        onSuccess()
                        Log.d(TAG, "onResponse: ${responseBody.message}")
                    }
                } else {
                    val responseError = response.errorBody()
                    responseError?.let {
                        val errorResponse = Gson().fromJson(it.string(), AuthResponse::class.java)
                        _errorToast.value = errorResponse.message
                        Log.e(TAG, "onFailureResponse: ${errorResponse.message}")
                    }
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                _errorToast.value = t.message
            }
        })
    }

    fun saveToken() {
        viewModelScope.launch {
            Log.d("AuthViewModel", "saveToken: ${_token.value}")
            pref.saveToken(_token.value!!)
        }
    }

    fun getToken(onTokenExist: () -> Unit, onTokenNotExist: () -> Unit) {
        viewModelScope.launch {
            pref.getToken().collect {
                _token.value = it

                if (_token.value.isNullOrEmpty()) {
                    onTokenNotExist()
                } else {
                    onTokenExist()
                }

            }
        }
    }

    companion object {
        private const val TAG = "AuthViewModel"
    }
}