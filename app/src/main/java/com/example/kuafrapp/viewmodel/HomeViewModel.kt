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

    fun refreshData() {

        val kaydedilmeZamani = PSharedPreferences.getTime()
        if (kaydedilmeZamani != null && kaydedilmeZamani != 0L && System.nanoTime() - kaydedilmeZamani < updateTime){
            //Sqlite'tan çek
            getDataFromSQLite()
        } else {
            getDataInternet()
        }
    }

    fun refreshDataFromInternet() {
        getDataInternet()
    }

    private fun getDataFromSQLite(){
        barberLoading.value = true

        viewModelScope.launch {

            val barbersList = BarberDatabase(getApplication()).barberDao().getAllBarber()
            showBarber(barbersList)
            Toast.makeText(getApplication(),"Besinleri Room'dan Aldık",Toast.LENGTH_LONG).show()

        }

    }

    private fun getDataInternet() {
        barberLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val barberLiveData = barberApiService.getData()
            withContext(Dispatchers.Main) {
                barberLoading.value = false
                getDataFromRoom(barberLiveData)
                Toast.makeText(getApplication(),"Besinleri Internet'ten Aldık",Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun showBarber(barberList: List<Barber>){
        barberLiveData.value = barberList
        barberError.value = false
        barberLoading.value = false
    }

    private fun getDataFromRoom(barberList: List<Barber>)  {

        viewModelScope.launch(Dispatchers.IO) {

            val dao = BarberDatabase(getApplication()).barberDao()
            dao.deleteAllBarber()
            val uuidList = dao.insertAll(barberList.toTypedArray())
            var i = 0
            while (i < barberList.size) {
                barberList[i].uuid = uuidList[i].toInt()
                i += 1
            }
            showBarber(barberList)
        }
        PSharedPreferences.saveTime(System.nanoTime())
    }

}