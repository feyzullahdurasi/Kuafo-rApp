package com.example.bakim.View.ServiceDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bakim.model.Service
import com.example.bakim.repository.BakimRepository
import com.example.bakim.model.Reservation
import com.example.bakim.model.ReservationRequest
import com.example.bakim.service.APIError
import com.example.bakim.service.APIResult
import kotlinx.coroutines.launch
import javax.inject.Inject

class ServiceDetailViewModel @Inject constructor(
    private val repository: BakimRepository
) : ViewModel() {
    
    private val _serviceDetails = MutableLiveData<APIResult<Service>>()
    val serviceDetails: LiveData<APIResult<Service>> = _serviceDetails
    
    private val _reservationResult = MutableLiveData<APIResult<Reservation>>()
    val reservationResult: LiveData<APIResult<Reservation>> = _reservationResult
    
    private val _showAlert = MutableLiveData(false)
    val showAlert: LiveData<Boolean> = _showAlert

    private val _alertMessage = MutableLiveData("")
    val alertMessage: LiveData<String> = _alertMessage

    fun loadServiceDetails(serviceId: Int, businessId: Int) {
        viewModelScope.launch {
            _serviceDetails.value = APIResult.Loading
            try {
                val result = repository.getServiceDetails(serviceId, businessId)
                _serviceDetails.value = result
            } catch (e: Exception) {
                _serviceDetails.value = APIResult.Error(APIError.UnableToComplete)
            }
        }
    }

    fun makeReservation(request: ReservationRequest) {
        viewModelScope.launch {
            _reservationResult.value = APIResult.Loading
            try {
                val result = repository.createReservation(request)
                _reservationResult.value = result
            } catch (e: Exception) {
                _reservationResult.value = APIResult.Error(APIError.UnableToComplete)
            }
        }
    }

    fun showAlert(message: String) {
        _alertMessage.value = message
        _showAlert.value = true
    }

    fun addUserComment(comment: String) {
        // Yorum ekleme işlemi
        viewModelScope.launch {
            try {
                // Yorum ekleme işlemini burada gerçekleştirin
                // Örneğin, repository.addComment(comment) gibi bir çağrı yapabilirsiniz
            } catch (e: Exception) {
                showAlert("Yorum eklenirken bir hata oluştu")
            }
        }
    }
}