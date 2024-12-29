package com.example.kuafrapp.repository

import com.example.kuafrapp.model.Business
import com.example.kuafrapp.model.Service
import com.example.kuafrapp.service.ApiResult
import com.example.kuafrapp.service.ApiService

class BakimRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getBusinesses(): ApiResult<List<Business>> {
        return try {
            val response = api.getBusinesses()
            if (response.isSuccessful) {
                ApiResult.Success(response.body()!!)
            } else {
                ApiResult.Error(ApiError.InvalidResponse)
            }
        } catch (e: Exception) {
            ApiResult.Error(ApiError.UnableToComplete)
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