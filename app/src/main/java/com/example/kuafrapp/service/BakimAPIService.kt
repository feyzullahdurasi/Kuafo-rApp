package com.example.kuafrapp.service

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.kuafrapp.R
import com.example.kuafrapp.model.Business
import com.example.kuafrapp.model.Reservation
import com.example.kuafrapp.model.ReservationRequest
import com.example.kuafrapp.model.Service
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

sealed class APIError(val userErrorMessage: String) {
    object NetworkError : APIError("İnternet bağlantınızı kontrol edin ve tekrar deneyin.")
    object InvalidURL : APIError("Geçersiz bir URL ile karşılaşıldı. Lütfen URL'yi kontrol edin.")
    object InvalidResponse : APIError("Sunucu yanıtında bir hata oluştu.")
    object UnableToComplete : APIError("İşlem tamamlanamadı. Lütfen daha sonra tekrar deneyin.")
    object InvalidData : APIError("Geçersiz veri formatı.")
    object ServerError : APIError("Sunucu hatası. Lütfen daha sonra tekrar deneyin.")
}

sealed class APIResult<out T> {
    data class Success<T>(val data: T) : APIResult<T>()
    data class Error(val error: APIError) : APIResult<Nothing>()
    object Loading : APIResult<Nothing>()
}

class UserErrorDialog @Inject constructor(private val context: Context) {
    fun showErrorDialog(error: APIError, onDismiss: () -> Unit) {
        val inflater = LayoutInflater.from(context)
        val dialogView: View = inflater.inflate(R.layout.dialog_user_error, null)

        dialogView.apply {
            findViewById<TextView>(R.id.errorTitle).text = "Hata"
            findViewById<TextView>(R.id.errorMessage).text = error.userErrorMessage
            findViewById<Button>(R.id.dismissButton).setOnClickListener {
                (context as? Activity)?.let { activity ->
                    if (!activity.isFinishing) {
                        onDismiss()
                    }
                }
            }
        }

        AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()
            .apply {
                setOnShowListener {
                    window?.setBackgroundDrawableResource(android.R.color.transparent)
                }
                show()
            }
    }
}

class BakimAPIService @Inject constructor(
    private val context: Context,
    private val api: ApiService
) {
    private val networkChecker = NetworkChecker(context)

    private suspend fun <T> safeApiCall(
        networkCheck: Boolean = true,
        apiCall: suspend () -> Response<T>
    ): APIResult<T> {
        if (networkCheck && !networkChecker.isNetworkAvailable()) {
            return APIResult.Error(APIError.NetworkError)
        }

        return try {
            val response = apiCall()
            when {
                response.isSuccessful -> {
                    response.body()?.let {
                        APIResult.Success(it)
                    } ?: APIResult.Error(APIError.InvalidData)
                }
                response.code() in 500..599 -> APIResult.Error(APIError.ServerError)
                else -> APIResult.Error(APIError.InvalidResponse)
            }
        } catch (e: Exception) {
            when (e) {
                is UnknownHostException -> APIResult.Error(APIError.NetworkError)
                is SocketTimeoutException -> APIResult.Error(APIError.UnableToComplete)
                else -> APIResult.Error(APIError.UnableToComplete)
            }
        }
    }

    suspend fun getServices(): APIResult<List<Service>> {
        return safeApiCall { api.getAllServices() }
    }

    suspend fun getBusinesses(): APIResult<List<Business>> {
        return safeApiCall { api.getBusinesses() }
    }

    suspend fun getBusinessServices(id: Int): APIResult<List<Service>> {
        return safeApiCall { api.getBusinessServices(id) }
    }

    suspend fun searchServices(query: String): APIResult<List<Service>> {
        return safeApiCall { api.searchServices(query) }
    }

    suspend fun getServiceDetails(serviceId: Int, businessId: Int): APIResult<Service> {
        return safeApiCall { api.getServiceDetails(serviceId, businessId) }
    }

    suspend fun createReservation(request: ReservationRequest): APIResult<Reservation> {
        return safeApiCall { api.createReservation(request) }
    }
}
