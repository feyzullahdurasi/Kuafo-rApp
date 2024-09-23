package com.example.kuafrapp.View.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns

class LoginViewModel : ViewModel() {

    private val _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?> = _emailError

    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> = _passwordError

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    fun validateEmail(email: String): Boolean {
        return when {
            email.isEmpty() -> {
                _emailError.value = "Bu alan boş bırakılamaz"
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _emailError.value = "Geçersiz e-posta adresi"
                false
            }
            else -> {
                _emailError.value = null
                true
            }
        }
    }

    fun validatePassword(password: String): Boolean {
        return when {
            password.isEmpty() -> {
                _passwordError.value = "Bu alan boş bırakılamaz"
                false
            }
            password.length < 8 -> {
                _passwordError.value = "Şifre en az 8 karakter olmalıdır"
                false
            }
            else -> {
                _passwordError.value = null
                true
            }
        }
    }

    fun onLoginClick(email: String, password: String) {
        val isEmailValid = validateEmail(email)
        val isPasswordValid = validatePassword(password)

        if (isEmailValid && isPasswordValid) {
            checkLoginCredentials(email, password)
        }
    }

    private fun checkLoginCredentials(email: String, password: String) {
        // Normalde bir API veya veritabanı ile kontrol yapılır.
        // Bu örnekte, giriş başarı koşulu "user@example.com" ve "password123" olarak simüle edilmiştir.
        if (email == "user@example.com" && password == "password123") {
            _loginSuccess.value = true
        } else {
            _loginSuccess.value = false
        }
    }
}
