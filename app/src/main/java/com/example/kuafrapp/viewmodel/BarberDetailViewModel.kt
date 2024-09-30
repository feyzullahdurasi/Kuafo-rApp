package com.example.kuafrapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kuafrapp.model.Barber
import com.example.kuafrapp.roomdb.BarberDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BarberDetailViewModel(application: Application) : AndroidViewModel(application) {

    // LiveData for observing barber data
    val barberLiveData = MutableLiveData<Barber?>()
    val isLoading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String?>()

    // Fetch barber data from Room database
    fun roomGetData(uuid: Int) {
        isLoading.value = true  // Set loading to true when data is being fetched
        viewModelScope.launch {
            try {
                // Access DAO for database operations
                val dao = BarberDatabase(getApplication()).barberDao()

                // Get barber by UUID (Primary Key)
                val barber = withContext(Dispatchers.IO) {
                    dao.getBarber(uuid)
                }

                // Check if barber is null
                if (barber != null) {
                    barberLiveData.value = barber
                } else {
                    errorMessage.value = "Kuaför bulunamadı."
                }
            } catch (e: Exception) {
                errorMessage.value = "Bir hata oluştu: ${e.localizedMessage}"
            } finally {
                isLoading.value = false  // Set loading to false after data is loaded
            }
        }
    }
}
