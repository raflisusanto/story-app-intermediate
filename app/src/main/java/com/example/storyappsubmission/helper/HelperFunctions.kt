package com.example.storyappsubmission.helper

import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout

fun setupEmailValidation(etEmail: EditText, tilEmail: TextInputLayout) {
    etEmail.doOnTextChanged { text, _, _, _ ->
        val isValidEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()
        if (!isValidEmail) {
            tilEmail.error = "Email tidak valid"
        } else {
            tilEmail.error = null
        }
    }
}

fun setupPasswordValidation(cvPassword: EditText, tilPassword: TextInputLayout) {
    cvPassword.doOnTextChanged { text, _, _, _ ->
        if (text.toString().length < 8) {
            tilPassword.error = "Password harus lebih dari 8 karakter"
        } else {
            tilPassword.error = null
        }
    }
}