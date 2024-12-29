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
    private val api: ApiService,
    private val db: BarberDatabase
) {
    suspend fun getBusinesses(): ApiResult<List<Business>> = withContext(Dispatchers.IO) {
        try {
            // Önce lokalden veri çek
            val localData = db.businessDao().getAllBusinesses()
            if (localData.isNotEmpty()) {
                return@withContext ApiResult.Success(localData)
            }

            // API'den veri çek
            val response = api.getBusinesses()
            if (response.isSuccessful) {
                response.body()?.let { businesses ->
                    // Verileri lokale kaydet
                    db.businessDao().insertAll(businesses)
                    ApiResult.Success(businesses)
                } ?: ApiResult.Error(APIError.InvalidData)
            } else {
                ApiResult.Error(APIError.InvalidResponse)
            }
        } catch (e: Exception) {
            ApiResult.Error(APIError.UnableToComplete)
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
}