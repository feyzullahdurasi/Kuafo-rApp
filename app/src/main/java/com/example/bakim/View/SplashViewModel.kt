package com.example.bakim.View

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val application: Application
) : AndroidViewModel(application) {

    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> = _loginStatus

    fun checkLoginStatus() {
        val sharedPreferences = getApplication<Application>().getSharedPreferences(
            "AppPreferences",
            Context.MODE_PRIVATE
        )
        _loginStatus.value = sharedPreferences.getBoolean("isLoggedIn", false)
    }
}