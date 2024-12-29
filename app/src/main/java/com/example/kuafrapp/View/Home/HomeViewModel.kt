package com.example.kuafrapp.View.Home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuafrapp.model.Business
import com.example.kuafrapp.repository.BakimRepository
import com.example.kuafrapp.service.APIResult
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
    val filteredServices: LiveData<List<Service>> get() = _filteredServices

    private val _businesses = MutableLiveData<APIResult<List<Business>>>()
    val businesses: LiveData<APIResult<List<Business>>> = _businesses

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

    fun loadServices() {
        viewModelScope.launch {
            _services.value = APIResult.Loading
            try {
                val result = repository.getServices()
                _services.value = result
            } catch (e: Exception) {
                _services.value = APIResult.Error(APIError.UnableToComplete)
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

    fun loadBusinesses() {
        viewModelScope.launch {
            _businesses.value = APIResult.Loading
            _businesses.value = repository.getBusinesses()
        }
    }
}