package com.example.kuafrapp.View.Home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kuafrapp.model.Business
import com.example.kuafrapp.model.MockData
import com.example.kuafrapp.model.Service

class HomeViewModel(application: Application) : AndroidViewModel(application) {
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
    val filteredServices: LiveData<List<Service>> get() = _filteredServices

    init {
        refreshData()
    }

    fun fetchServices() {
        // Simulate fetching
        _businessLiveData.postValue(MockData.sampleServices)
        applyFilter(null) // Default all services
    }

    fun applyFilter(serviceType: String?) {
        val allServices = _businessLiveData.value?.services ?: emptyList()
        _filteredServices.postValue(
            if (serviceType.isNullOrEmpty()) allServices
            else allServices.filter { it.serviceType == serviceType }
        )
    }

    fun refreshData() {
        _isLoading.value = true
        try {
            // MockData'dan veri al
            val mockBusiness = MockData.sampleBusiness
            _businessLiveData.value = mockBusiness
            _selectedServiceType.value = null
            _isLoading.value = false
            _hasError.value = false
        } catch (e: Exception) {
            _hasError.value = true
            _error.value = "Veri yüklenirken hata oluştu: ${e.localizedMessage}"
            _isLoading.value = false
        }
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
            "4.8" // Örnek bir rating
        } else {
            "Price varies"
        }
    }
}