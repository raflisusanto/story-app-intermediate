package com.example.storyappsubmission.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.storyappsubmission.R
import com.example.storyappsubmission.data.request.AuthRequest
import com.example.storyappsubmission.data.response.AuthResponse
import com.example.storyappsubmission.data.retrofit.ApiConfig
import com.example.storyappsubmission.databinding.ActivityMainBinding
import com.example.storyappsubmission.helper.setupCombinedText
import com.example.storyappsubmission.helper.setupEmailValidation
import com.example.storyappsubmission.helper.setupPasswordValidation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEmailValidation(binding.etEmail, binding.tilEmail!!)
        setupPasswordValidation(binding.cvPassword, binding.tilPassword)
        setupCombinedText(binding.tvTailing!!, R.string.login_tail_desc, RegisterActivity::class.java)

        binding.btnLogin.setOnClickListener {
            // todo: post
            val email = binding.etEmail.text.toString()
            val password = binding.cvPassword.text.toString()
            val isNotEmpty: Boolean = email.isNotEmpty() && password.isNotEmpty()
            val isNotError: Boolean = binding.etEmail.error == null && binding.cvPassword.error == null

            if (isNotEmpty && isNotError) {
                val loginRequest = AuthRequest(email = email, password = password)
                val client = ApiConfig.getApiService().login(loginRequest)

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
                            Log.e(TAG, "onResponseFailer: ${response.message()}")
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
        private const val TAG = "MainActivity"
    }
}