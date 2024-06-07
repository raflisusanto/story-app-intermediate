package com.example.storyappsubmission.data.remote.response

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @field: SerializedName("error")
    val error: Boolean,

    @field: SerializedName("message")
    val message: String,

    @field: SerializedName("loginResult")
    val loginResult: User? = null
)

data class User(
    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("token")
    val token: String,
)