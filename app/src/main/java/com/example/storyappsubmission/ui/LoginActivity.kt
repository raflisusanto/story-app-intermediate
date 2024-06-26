package com.example.storyappsubmission.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.storyappsubmission.R
import com.example.storyappsubmission.databinding.ActivityLoginBinding
import com.example.storyappsubmission.helper.setupCombinedText
import com.example.storyappsubmission.helper.setupEmailValidation
import com.example.storyappsubmission.helper.showToast
import com.example.storyappsubmission.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.btnSubmit.setButtonLabel("Login")
        setContentView(binding.root)

        // Validation in TextInputLayout
        setupEmailValidation(binding.etEmail, binding.tilEmail)
        setupCombinedText(
            binding.tvTailing,
            R.string.login_tail_desc,
            RegisterActivity::class.java
        )

        // ViewModels
        authViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.btnSubmit.showLoading()
            } else {
                binding.btnSubmit.hideLoading()
            }
        }

        authViewModel.errorToast.observe(this) { errorMessage ->
            errorMessage?.showToast(this)
        }

        // Login
        binding.btnSubmit.setOnClick {
            val email = binding.etEmail.text.toString()
            val password = binding.cvPassword.text.toString()

            authViewModel.login(email, password) {
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }
}