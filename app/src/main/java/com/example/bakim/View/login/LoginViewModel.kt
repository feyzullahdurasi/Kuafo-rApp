package com.example.bakim.View.login

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.example.bakim.util.Resource
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?> = _emailError

    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> = _passwordError

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _googleSignInResult = MutableLiveData<Resource<GoogleSignInAccount>>()
    val googleSignInResult: LiveData<Resource<GoogleSignInAccount>> = _googleSignInResult

    private val _authToken = MutableLiveData<String>()
    val authToken: LiveData<String> = _authToken

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

    fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            _googleSignInResult.value = Resource.Success(account)
            
            // Google bilgilerini kaydet
            saveGoogleUserInfo(account)
            
            // Başarılı giriş
            _loginSuccess.value = true
            
        } catch (e: ApiException) {
            _googleSignInResult.value = Resource.Error(e.message ?: "Google Sign-In failed")
            _loginSuccess.value = false
        }
    }

    private fun saveGoogleUserInfo(account: GoogleSignInAccount) {
        PreferenceManager.getDefaultSharedPreferences(getApplication())
            .edit()
            .putString("user_email", account.email)
            .putString("user_name", account.displayName)
            .putString("user_photo", account.photoUrl?.toString())
            .putString("google_id", account.id)
            .apply()
    }
}
