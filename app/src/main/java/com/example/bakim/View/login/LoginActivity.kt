package com.example.bakim.View.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bakim.R
import com.example.bakim.View.Info.InfoActivity
import com.example.bakim.View.register.RegisterActivity
import com.example.bakim.databinding.ActivityLoginBinding
import com.example.bakim.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.loginButton.setOnClickListener {
            val email = binding.eMailEt.text.toString()
            val password = binding.passwordEt.text.toString()
            viewModel.login(email, password)
        }

        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(this) { result ->
            when (result) {
                is Resource.Success -> {
                    saveLoginState(true)
                    startActivity(Intent(this, InfoActivity::class.java))
                    finish()
                }
                is Resource.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    // Yükleme göstergesi gösterilebilir
                }
            }
        }
    }

    private fun saveLoginState(isLoggedIn: Boolean) {
        getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("isLoggedIn", isLoggedIn)
            .apply()
    }
}
