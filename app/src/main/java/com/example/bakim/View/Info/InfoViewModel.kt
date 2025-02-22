package com.example.bakim.View.Info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InfoViewModel : ViewModel() {

    private val _navigateToMain = MutableLiveData<String?>()
    val navigateToMain: LiveData<String?> get() = _navigateToMain

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun selectService(serviceType: String) {
        _isLoading.value = true
        try {
            // Simulate service selection logic
            _navigateToMain.value = serviceType
        } catch (e: Exception) {
            _errorMessage.value = e.message
        } finally {
            _isLoading.value = false
        }
    }

    fun resetNavigation() {
        _navigateToMain.value = null
    }
}
