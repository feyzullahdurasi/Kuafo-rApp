package com.example.kuafrapp.service

import com.example.kuafrapp.model.Barber
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BarberAPIService {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(BarberAPI::class.java)

    suspend fun getData(): List<Barber> {
        return retrofit.getBarber()
    }
}