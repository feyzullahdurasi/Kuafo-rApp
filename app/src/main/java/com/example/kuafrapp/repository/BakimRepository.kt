package com.example.kuafrapp.repository

import com.example.kuafrapp.model.Business
import com.example.kuafrapp.model.Service
import com.example.kuafrapp.roomdb.BarberDatabase
import com.example.kuafrapp.service.APIError
import com.example.kuafrapp.service.ApiResult
import com.example.kuafrapp.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BakimRepository @Inject constructor(
    private val apiService: BakimAPIService
) {
    suspend fun getBusinesses(): APIResult<List<Business>> {
        return try {
            val response = apiService.getBusinesses()
            if (response.isSuccessful) {
                APIResult.Success(response.body() ?: emptyList())
            } else {
                APIResult.Error(APIError.ServerError)
            }
        } catch (e: Exception) {
            APIResult.Error(APIError.NetworkError)
        }
    }

    suspend fun getBusinessServices(id: Int): ApiResult<List<Service>> {
        return try {
            val response = api.getBusinessServices(id)
            if (response.isSuccessful) {
                ApiResult<List<Service>>().apply {
                    data = response.body()
                    error = null
                }
            } else {
                ApiResult<List<Service>>().apply {
                    data = null
                    error = response.errorBody()?.string()
                }
            }
        } catch (e: Exception) {
            ApiResult<List<Service>>().apply {
                data = null
                error = e.localizedMessage
            }
        }
    }

    suspend fun getServiceDetails(serviceId: Int, businessId: Int): APIResult<Service> {
        return try {
            val response = apiService.getServiceDetails(serviceId, businessId)
            if (response.isSuccessful) {
                APIResult.Success(response.body() ?: throw Exception("Boş yanıt"))
            } else {
                APIResult.Error(APIError.ServerError)
            }
        } catch (e: Exception) {
            APIResult.Error(APIError.NetworkError)
        }
    }

    suspend fun createReservation(request: ReservationRequest): APIResult<Reservation> {
        return try {
            val response = apiService.createReservation(request)
            if (response.isSuccessful) {
                APIResult.Success(response.body() ?: throw Exception("Boş yanıt"))
            } else {
                APIResult.Error(APIError.ServerError)
            }
        } catch (e: Exception) {
            APIResult.Error(APIError.NetworkError)
        }
    }
}