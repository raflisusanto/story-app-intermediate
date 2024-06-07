package com.example.storyappsubmission.domain.repository

import com.example.storyappsubmission.data.remote.response.AuthResponse
import com.example.storyappsubmission.data.remote.response.ErrorResponse

interface AuthRepository {
    fun login(
        email: String,
        password: String,
        onSuccess: (AuthResponse) -> Unit,
        onError: (ErrorResponse) -> Unit
    )

    fun register(
        name: String,
        email: String,
        password: String,
        onSuccess: (AuthResponse) -> Unit,
        onError: (ErrorResponse) -> Unit
    )
}