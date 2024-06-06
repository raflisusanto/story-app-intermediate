package com.example.storyappsubmission.data.retrofit

import com.example.storyappsubmission.data.request.AuthRequest
import com.example.storyappsubmission.data.response.AuthResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("login")
    fun login(
        @Body loginRequest: AuthRequest,
    ): Call<AuthResponse>

    @POST("register")
    fun register(
        @Body loginRequest: AuthRequest,
    ): Call<AuthResponse>
}