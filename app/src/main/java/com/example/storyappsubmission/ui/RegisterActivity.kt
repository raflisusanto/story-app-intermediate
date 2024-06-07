package com.example.storyappsubmission.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.storyappsubmission.R
import com.example.storyappsubmission.data.TokenManager
import com.example.storyappsubmission.data.dataStore
import com.example.storyappsubmission.databinding.ActivityRegisterBinding
import com.example.storyappsubmission.helper.ViewModelFactory
import com.example.storyappsubmission.helper.setupCombinedText
import com.example.storyappsubmission.helper.setupEmailValidation
import com.example.storyappsubmission.helper.setupPasswordValidation
import com.example.storyappsubmission.helper.showToast
import com.example.storyappsubmission.viewmodel.AuthViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        binding.btnSubmit.setButtonLabel("Daftar")
        setContentView(binding.root)

        setupPasswordValidation(binding.cvPassword, binding.tilPassword)
        setupEmailValidation(binding.etEmail, binding.tilEmail)
        setupCombinedText(binding.tvTailing, R.string.register_tail_desc, MainActivity::class.java)

        // ViewModels
        val pref = TokenManager.getInstance(application.dataStore)
        val factory = ViewModelFactory.getInstance(pref)
        val authViewModel = ViewModelProvider(
            this,
            factory
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

        binding.btnSubmit.setOnClick {
            val email = binding.etEmail.text.toString()
            val password = binding.cvPassword.text.toString()
            val name = binding.etName.text.toString()

            val isNotEmpty: Boolean =
                email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()
            val isNotError: Boolean =
                binding.etEmail.error == null && binding.cvPassword.error == null

            if (isNotEmpty && isNotError) {
                authViewModel.register(name, email, password) {
                    "Pendaftaran Berhasil, silahkan Login".showToast(this@RegisterActivity)
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                }
            } else {
                val errorMessage = "Data tidak valid, coba isi lagi ya"
                errorMessage.showToast(this)
            }
        }
    }
}