package com.example.storyappsubmission.data.remote.retrofit

import com.example.storyappsubmission.data.remote.request.AuthRequest
import com.example.storyappsubmission.data.remote.response.AuthResponse
import retrofit2.Call
import retrofit2.http.*

interface AuthService {
    @POST("login")
    fun login(
        @Body loginRequest: AuthRequest,
    ): Call<AuthResponse>

    @POST("register")
    fun register(
        @Body loginRequest: AuthRequest,
    ): Call<AuthResponse>
}