package com.example.kuafrapp.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kuafrapp.model.Barber
import com.example.kuafrapp.roomdb.BarberDatabase
import com.example.kuafrapp.service.BarberAPIService
import com.example.kuafrapp.util.PSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    val barberLiveData = MutableLiveData<List<Barber>>()
    val barberError = MutableLiveData<Boolean>()
    val barberLoading = MutableLiveData<Boolean>()
    private var updateTime = 10 * 60 * 1000 * 1000 * 1000L

    private val barberApiService = BarberAPIService()
    private val PSharedPreferences = PSharedPreferences(getApplication())

    // Verileri yenile
    fun refreshData(s: String) {
        val kaydedilmeZamani = PSharedPreferences.getTime()
        if (kaydedilmeZamani != null && kaydedilmeZamani != 0L && System.nanoTime() - kaydedilmeZamani < updateTime) {
            // SQLite'tan veriyi çek
            getDataFromSQLite()
        } else {
            getDataInternet()
        }
    }

    // Internet'ten yenile
    fun refreshDataFromInternet(serviceType: String?) {
        getDataInternet()
    }

    // SQLite'tan veri çekme
    private fun getDataFromSQLite() {
        barberLoading.value = true
        viewModelScope.launch {
            val barbersList = BarberDatabase(getApplication()).barberDao().getAllBarber()
            withContext(Dispatchers.Main) {
                showBarber(barbersList)
                Toast.makeText(getApplication(), "Veriler Room'dan alındı", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Internet'ten veri çekme
    private fun getDataInternet() {
        barberLoading.value = true

        viewModelScope.launch {
            try {
                // API'den veri al
                val barberList = barberApiService.getData()

                // Ana thread'e dön ve UI güncelle
                withContext(Dispatchers.Main) {
                    if (barberList.isNotEmpty()) {
                        getDataFromRoom(barberList)
                        Toast.makeText(getApplication(), "Veriler Internet'ten alındı", Toast.LENGTH_LONG).show()
                    } else {
                        barberError.value = true
                        barberLoading.value = false
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    barberError.value = true
                    barberLoading.value = false
                    Toast.makeText(getApplication(), "Bir hata oluştu: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // Barber listesi UI'ya göster
    private fun showBarber(barberList: List<Barber>) {
        barberLiveData.value = barberList
        barberError.value = false
        barberLoading.value = false
    }

    // Verileri Room veritabanına kaydetme
    private fun getDataFromRoom(barberList: List<Barber>) {
        viewModelScope.launch(Dispatchers.IO) {
            val dao = BarberDatabase(getApplication()).barberDao()
            dao.deleteAllBarber()
            val uuidList = dao.insertAll(barberList.toTypedArray())

            // UUID'leri güncelle
            for (i in barberList.indices) {
                barberList[i].uuid = uuidList[i].toInt()
            }

            // Ana thread'e dön ve UI güncelle
            withContext(Dispatchers.Main) {
                showBarber(barberList)
            }
        }
        PSharedPreferences.saveTime(System.nanoTime())
    }
}
