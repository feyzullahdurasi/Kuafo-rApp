package com.example.bakim.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bakim.View.Info.InfoActivity
import com.example.bakim.View.login.LoginActivity
import com.example.bakim.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModel.checkLoginStatus()
        viewModel.loginStatus.observe(this) { isLoggedIn ->
            if (isLoggedIn) {
                navigateToInfo()
            } else {
                navigateToLogin()
            }
        }
    }

    private fun navigateToInfo() {
        startActivity(Intent(this, InfoActivity::class.java))
        finish()
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
