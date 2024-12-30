package com.example.kuafrapp.repository

import com.example.kuafrapp.model.Business
import com.example.kuafrapp.model.Reservation
import com.example.kuafrapp.model.ReservationRequest
import com.example.kuafrapp.model.Service
import com.example.kuafrapp.service.APIResult
import com.example.kuafrapp.service.BakimAPIService
import javax.inject.Inject

class BakimRepository @Inject constructor(
    private val bakimApi: BakimAPIService
) {
    suspend fun getBusinessServices(businessId: Int): APIResult<List<Service>> {
        return bakimApi.getBusinessServices(businessId)
    }

    suspend fun getServiceDetails(serviceId: Int, businessId: Int): APIResult<Service> {
        return bakimApi.getServiceDetails(serviceId, businessId)
    }

    suspend fun createReservation(request: ReservationRequest): APIResult<Reservation> {
        return bakimApi.createReservation(request)
    }

    suspend fun getServices(): APIResult<List<Service>> {
        return bakimApi.getServices()
    }

    suspend fun searchServices(query: String): APIResult<List<Service>> {
        return bakimApi.searchServices(query)
    }

    suspend fun getBusinesses(): APIResult<List<Business>> {
        return bakimApi.getBusinesses()
    }
}