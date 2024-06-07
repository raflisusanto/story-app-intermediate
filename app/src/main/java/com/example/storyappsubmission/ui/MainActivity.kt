package com.example.storyappsubmission.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.storyappsubmission.R
import com.example.storyappsubmission.databinding.ActivityMainBinding
import com.example.storyappsubmission.helper.setupCombinedText
import com.example.storyappsubmission.helper.setupEmailValidation
import com.example.storyappsubmission.helper.setupPasswordValidation
import com.example.storyappsubmission.helper.showToast
import com.example.storyappsubmission.viewmodel.AuthViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.btnSubmit.setButtonLabel("Login")
        setContentView(binding.root)

        // Validation in TextInputLayout
        setupEmailValidation(binding.etEmail, binding.tilEmail)
        setupPasswordValidation(binding.cvPassword, binding.tilPassword)
        setupCombinedText(
            binding.tvTailing,
            R.string.login_tail_desc,
            RegisterActivity::class.java
        )

        // ViewModels
        val authViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[AuthViewModel::class.java]

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
            val isNotEmpty: Boolean = email.isNotEmpty() && password.isNotEmpty()
            val isNotError: Boolean =
                binding.etEmail.error == null && binding.cvPassword.error == null

            if (isNotEmpty && isNotError) {
                authViewModel.login(email, password)
            } else {
                val errorMessage = "Data tidak valid, coba isi lagi ya"
                errorMessage.showToast(this)
            }
        }
    }
}