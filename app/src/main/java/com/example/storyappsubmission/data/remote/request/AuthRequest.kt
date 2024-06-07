package com.example.storyappsubmission.data.remote.request

data class AuthRequest(
    val email: String,
    val password: String,
    val name: String? = null,
)