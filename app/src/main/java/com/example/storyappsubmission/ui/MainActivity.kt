package com.example.storyappsubmission.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.storyappsubmission.data.TokenManager
import com.example.storyappsubmission.data.dataStore
import com.example.storyappsubmission.helper.ViewModelFactory
import com.example.storyappsubmission.ui.view.HomeActivity
import com.example.storyappsubmission.viewmodel.AuthViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref = TokenManager.getInstance(application.dataStore)
        val factory = ViewModelFactory.getInstance(pref)
        val authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        authViewModel.getToken({
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
        }, {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        })
    }
}