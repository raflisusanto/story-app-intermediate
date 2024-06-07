package com.example.storyappsubmission.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.storyappsubmission.ui.view.HomeActivity
import com.example.storyappsubmission.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authViewModel.getToken({
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
        }, {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        })
    }
}