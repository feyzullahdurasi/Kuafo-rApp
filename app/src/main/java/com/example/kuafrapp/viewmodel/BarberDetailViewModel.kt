package com.example.kuafrapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuafrapp.model.Barber
import com.example.kuafrapp.roomdb.BarberDatabase
import com.example.kuafrapp.service.BarberAPIService
import com.example.kuafrapp.util.PSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BarberDetailViewModel(application: Application) : AndroidViewModel(application) {

    val barberLiveData = MutableLiveData<Barber>()

    fun roomGetData(uuid : Int){
        viewModelScope.launch {
            val dao = BarberDatabase(getApplication()).barberDao()
            val barber = dao.getBarber(uuid)
            barberLiveData.value = barber
        }
    }
}