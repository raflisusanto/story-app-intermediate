package com.example.storyappsubmission.data.repository

import com.example.storyappsubmission.data.remote.request.AuthRequest
import com.example.storyappsubmission.data.remote.response.AuthResponse
import com.example.storyappsubmission.data.remote.response.ErrorResponse
import com.example.storyappsubmission.data.remote.retrofit.AuthService
import com.example.storyappsubmission.domain.repository.AuthRepository
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepositoryImpl(
    private val authService: AuthService
) : AuthRepository {

    override fun login(
        email: String,
        password: String,
        onSuccess: (AuthResponse) -> Unit,
        onError: (ErrorResponse) -> Unit
    ) {
        val loginRequest = AuthRequest(email = email, password = password)
        val client = authService.login(loginRequest)

        client.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(
                call: Call<AuthResponse>,
                response: Response<AuthResponse>
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

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                val errorResponse = ErrorResponse(message = t.message)
                onError(errorResponse)
            }
        })
    }

    override fun register(
        name: String, email: String, password: String, onSuccess: (AuthResponse) -> Unit,
        onError: (ErrorResponse) -> Unit
    ) {
        val registerRequest = AuthRequest(name = name, email = email, password = password)
        val client = authService.register(registerRequest)

        client.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(
                call: Call<AuthResponse>,
                response: Response<AuthResponse>
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

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                val errorResponse = ErrorResponse(message = t.message)
                onError(errorResponse)
            }
        })
    }
}