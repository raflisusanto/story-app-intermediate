package com.example.storyappsubmission.data.request

data class AuthRequest(
    val email: String,
    val password: String,
    val name: String? = null,
)