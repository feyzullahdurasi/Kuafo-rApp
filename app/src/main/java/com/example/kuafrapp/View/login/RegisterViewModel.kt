package com.example.kuafrapp.View.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns

class RegisterViewModel : ViewModel() {

    private val _fullNameError = MutableLiveData<String?>()
    val fullNameError: LiveData<String?> = _fullNameError

    private val _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?> = _emailError

    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> = _passwordError

    private val _rePasswordError = MutableLiveData<String?>()
    val rePasswordError: LiveData<String?> = _rePasswordError

    private val _passwordMatch = MutableLiveData<Boolean>()
    val passwordMatch: LiveData<Boolean> = _passwordMatch

    private val _navigateToInfo = MutableLiveData<Boolean>()
    val navigateToInfo: LiveData<Boolean> = _navigateToInfo

    fun validateFullName(fullName: String): Boolean {
        return if (fullName.isEmpty()) {
            _fullNameError.value = "Bu alan boş bırakılamaz"
            false
        } else {
            _fullNameError.value = null
            true
        }
    }

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

    fun validateRePassword(rePassword: String): Boolean {
        return when {
            rePassword.isEmpty() -> {
                _rePasswordError.value = "Bu alan boş bırakılamaz"
                false
            }
            rePassword.length < 8 -> {
                _rePasswordError.value = "Şifre en az 8 karakter olmalıdır"
                false
            }
            else -> {
                _rePasswordError.value = null
                true
            }
        }
    }

    fun validatePasswordMatch(password: String, rePassword: String) {
        _passwordMatch.value = password == rePassword && password.isNotEmpty()
    }

    fun checkUserRegistration(email: String) {
        // This is where you would typically check against a database or API
        // For this example, we'll simulate that a user with email "test@example.com" is already registered
        if (email == "test@example.com") {
            _navigateToInfo.value = true
        } else {
            _navigateToInfo.value = false
        }
    }

    fun onRegisterClick(fullName: String, email: String, password: String, rePassword: String) {
        val isFullNameValid = validateFullName(fullName)
        val isEmailValid = validateEmail(email)
        val isPasswordValid = validatePassword(password)
        val isRePasswordValid = validateRePassword(rePassword)
        val doPasswordsMatch = password == rePassword

        if (isFullNameValid && isEmailValid && isPasswordValid && isRePasswordValid && doPasswordsMatch) {
            checkUserRegistration(email)
        }
    }
}