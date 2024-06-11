package com.example.storyappsubmission.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.storyappsubmission.R
import com.example.storyappsubmission.databinding.ActivityRegisterBinding
import com.example.storyappsubmission.helper.setupCombinedText
import com.example.storyappsubmission.helper.setupEmailValidation
import com.example.storyappsubmission.helper.showToast
import com.example.storyappsubmission.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        binding.btnSubmit.setButtonLabel("Daftar")
        setContentView(binding.root)

        setupEmailValidation(binding.etEmail, binding.tilEmail)
        setupCombinedText(binding.tvTailing, R.string.register_tail_desc, MainActivity::class.java)

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

            authViewModel.register(name, email, password) {
                "Pendaftaran Berhasil, silahkan Login".showToast(this@RegisterActivity)
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
        }
    }
}