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
    application: Application,
    private val bakimRepository: BakimRepository
) : AndroidViewModel(application) {

    private val _loginResult = MutableLiveData<Resource<Unit>>()
    val loginResult: LiveData<Resource<Unit>> = _loginResult

    private val _googleSignInResult = MutableLiveData<Resource<GoogleSignInAccount>>()
    val googleSignInResult: LiveData<Resource<GoogleSignInAccount>> = _googleSignInResult

    private val _authToken = MutableLiveData<String>()
    val authToken: LiveData<String> = _authToken

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    fun login(email: String, password: String) {
        if (!validateEmail(email)) {
            _loginResult.value = Resource.Error("Geçersiz email formatı")
            return
        }

        if (!validatePassword(password)) {
            _loginResult.value = Resource.Error("Şifre en az 6 karakter olmalıdır")
            return
        }

        // Burada gerçek API çağrısı yapılacak
        if (email == "test@test.com" && password == "123456") {
            _loginResult.value = Resource.Success(Unit)
        } else {
            _loginResult.value = Resource.Error("Email veya şifre hatalı")
        }
    }

    private fun validateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validatePassword(password: String): Boolean {
        return password.length >= 6
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
