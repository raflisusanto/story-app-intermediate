package com.example.storyappsubmission.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.storyappsubmission.R
import com.example.storyappsubmission.data.request.AuthRequest
import com.example.storyappsubmission.data.response.AuthResponse
import com.example.storyappsubmission.data.retrofit.ApiConfig
import com.example.storyappsubmission.databinding.ActivityRegisterBinding
import com.example.storyappsubmission.helper.setupCombinedText
import com.example.storyappsubmission.helper.setupEmailValidation
import com.example.storyappsubmission.helper.setupPasswordValidation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupPasswordValidation(binding.cvPassword, binding.tilPassword)
        setupEmailValidation(binding.etEmail, binding.tilEmail)
        setupCombinedText(binding.tvTailing, R.string.register_tail_desc, MainActivity::class.java)

        binding.btnRegister.setOnClickListener {
            // todo: post
            val email = binding.etEmail.text.toString()
            val password = binding.cvPassword.text.toString()
            val name = binding.etName.text.toString()

            val isNotEmpty: Boolean = email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()
            val isNotError: Boolean = binding.etEmail.error == null && binding.cvPassword.error == null

            if (isNotEmpty && isNotError) {
                val registerRequest = AuthRequest(email = email, password = password, name = name)
                val client = ApiConfig.getApiService().register(registerRequest)

                client.enqueue(object : Callback<AuthResponse> {
                    override fun onResponse(
                        call: Call<AuthResponse>,
                        response: Response<AuthResponse>
                    ) {
//                        _isLoading.value = false
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null) {
                                // save to shared preference
                                Log.d(TAG, "onResponse: ${responseBody.message}")
                            }
                        } else {
                            Log.e(TAG, "onFailureResponse: ${response.message()} ${response.body()}")
//                            _errorToast.value = response.message()
                        }
                    }
                    override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
//                        _isLoading.value = false
                        Log.e(TAG, "onFailure: ${t.message}")
//                        _errorToast.value = t.message
                    }
                })
            }


        }
    }

    companion object{
        private const val TAG = "RegisterActivity"
    }
}