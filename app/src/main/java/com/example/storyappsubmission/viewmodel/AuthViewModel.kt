package com.example.storyappsubmission.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyappsubmission.data.request.AuthRequest
import com.example.storyappsubmission.data.response.AuthResponse
import com.example.storyappsubmission.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorToast = MutableLiveData<String>()
    val errorToast: LiveData<String> = _errorToast

    fun login(email: String, password: String) {
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
                        // save to shared preference
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
    }

    fun register(name: String, email: String, password: String) {
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
                        // save to shared preference
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
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}