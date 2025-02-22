package com.example.bakim.View.Home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bakim.model.Business
import com.example.bakim.model.Service
import com.example.bakim.repository.BakimRepository
import com.example.bakim.service.APIError
import com.example.bakim.service.APIResult
import com.example.bakim.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: BakimRepository
) : ViewModel() {

    private val _services = MutableLiveData<APIResult<List<Service>>>()
    val services: LiveData<APIResult<List<Service>>> = _services

    private val _businessLiveData = MutableLiveData<Business?>()
    val businessLiveData: LiveData<Business?> = _businessLiveData

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _hasError = MutableLiveData(false)
    val hasError: LiveData<Boolean> = _hasError

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private val _selectedService = MutableLiveData<Service?>(null)
    val selectedService: LiveData<Service?> = _selectedService

    private val _selectedServiceType = MutableLiveData<String?>(null)
    val selectedServiceType: LiveData<String?> = _selectedServiceType

    private val _filteredServices = MutableLiveData<List<Service>>()
    val filteredServices: LiveData<List<Service>> = _filteredServices

    private val _businesses = MutableLiveData<APIResult<List<Business>>>()
    val businesses: LiveData<APIResult<List<Business>>> = _businesses

    init {
        loadServices()
        loadBusinesses()
    }

    fun fetchServices() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                when (val result = repository.getServices()) {
                    is APIResult.Success -> {
                        _services.value = result
                        applyFilter(null)
                    }
                    is APIResult.Error -> {
                        _error.value = result.error.userErrorMessage
                        _hasError.value = true
                    }
                    is APIResult.Loading -> {
                        // Loading state is handled separately
                    }
                }
            } catch (e: Exception) {
                _error.value = "Unexpected error: ${e.message}"
                _hasError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun applyFilter(serviceType: String?) {
        val currentServices = (_services.value as? APIResult.Success)?.data ?: return
        val filteredList = if (serviceType.isNullOrEmpty()) {
            currentServices
        } else {
            currentServices.filter { it.serviceType == serviceType }
        }
        _filteredServices.value = filteredList
    }

    fun refreshData() {
        loadServices()
        loadBusinesses()
    }

    fun selectService(service: Service) {
        _selectedService.value = service
        _selectedServiceType.value = service.serviceType
    }

    fun getServicePriceRange(service: Service): String {
        val prices = service.serviceFeature.map { it.price }
        return if (prices.isNotEmpty()) {
            val minPrice = prices.minOrNull()
            val maxPrice = prices.maxOrNull()
            "$minPrice - $maxPrice"
        } else {
            "Price varies"
        }
    }

    private fun loadServices() {
        viewModelScope.launch {
            _services.value = APIResult.Loading
            try {
                val result = repository.getServices()
                _services.value = result
            } catch (e: Exception) {
                _services.value = APIResult.Error(APIError.InvalidData)
            }
        }
    }

    fun searchServices(query: String) {
        viewModelScope.launch {
            _services.value = APIResult.Loading
            try {
                val result = repository.searchServices(query)
                _services.value = result
            } catch (e: Exception) {
                _services.value = APIResult.Error(APIError.UnableToComplete)
            }
        }
    }

    private fun loadBusinesses() {
        viewModelScope.launch {
            _businesses.value = APIResult.Loading
            try {
                val result = repository.getBusinesses()
                _businesses.value = result
            } catch (e: Exception) {
                _businesses.value = APIResult.Error(APIError.InvalidData)
            }
        }
    }
}

class UserErrorDialog @Inject constructor(private val context: Context) {
    fun showErrorDialog(error: APIError, onDismiss: () -> Unit) {
        val inflater = LayoutInflater.from(context)
        val dialogView: View = inflater.inflate(R.layout.dialog_user_error, null)

        dialogView.apply {
            findViewById<TextView>(R.id.errorTitle).text = "deneme"//context.getString(R.string.error_title)
            findViewById<TextView>(R.id.errorMessage).text = error.userErrorMessage
            findViewById<Button>(R.id.dismissButton).setOnClickListener {
                onDismiss()
            }
        }

        AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()
            .apply {
                setOnShowListener {
                    window?.setBackgroundDrawableResource(android.R.color.transparent)
                }
                show()
            }
    }
}
