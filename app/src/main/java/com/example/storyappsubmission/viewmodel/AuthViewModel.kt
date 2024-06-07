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
    val token: LiveData<String> = _token

    init {
        _token.value = pref.getToken().toString()
    }

    fun login(email: String, password: String): Boolean {
        _isLoading.value = true

        val loginRequest = AuthRequest(email = email, password = password)
        val client = ApiConfig.getApiService().login(loginRequest)
        var token : String? = null

        client.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(
                call: Call<AuthResponse>,
                response: Response<AuthResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        token = responseBody.loginResult?.token
                        Log.d(TAG, "onResponse: ${responseBody.message}")
                    }
                } else {
                    Log.e(TAG, "onResponseFailure: ${response.message()}")
                    _errorToast.value = response.message()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                _errorToast.value = t.message
            }
        })

        if (token != null) {
            viewModelScope.launch {
                pref.saveToken(token!!)
                _token.value = token!!
            }

            return true
        }

        return false
    }

    fun register(name: String, email: String, password: String): Boolean {
        _isLoading.value = true

        val registerRequest = AuthRequest(email = email, password = password, name = name)
        val client = ApiConfig.getApiService().register(registerRequest)
        var registerSuccess = false

        client.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(
                call: Call<AuthResponse>,
                response: Response<AuthResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        registerSuccess = true
                        Log.d(TAG, "onResponse: ${responseBody.message}")
                    }
                } else {
                    Log.e(TAG, "onFailureResponse: ${response.message()} ${response.body()}")
                    _errorToast.value = response.message()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                _errorToast.value = t.message
            }
        })

        return registerSuccess
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}