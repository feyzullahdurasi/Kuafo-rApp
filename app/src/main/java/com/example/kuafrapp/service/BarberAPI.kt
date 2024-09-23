package com.example.kuafrapp.service

import com.example.kuafrapp.model.Barber
import retrofit2.http.GET

interface BarberAPI {

    //GET, POST

    //https://raw.githubusercontent.com/atilsamancioglu/BTK20-JSONVeriSeti/master/besinler.json

    @GET("atilsamancioglu/BTK20-JSONVeriSeti/master/besinler.json")
    suspend fun getBarber(): List<Barber>
}