package com.example.kuafrapp.service

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.kuafrapp.R
import com.example.kuafrapp.model.Business
import com.example.kuafrapp.model.Reservation
import com.example.kuafrapp.model.Service
import javax.inject.Inject

sealed class APIError(val userErrorMessage: String) {
    object InvalidURL : APIError("Geçersiz bir URL ile karşılaşıldı. Lütfen URL'yi kontrol edin.")
    object InvalidResponse : APIError("Sunucu yanıtında bir hata oluştu.")
    object UnableToComplete : APIError("İnternette bir hata oluştu. Lütfen daha sonra tekrar deneyin.")
    object InvalidData : APIError("Geçersiz veri.")
}

sealed class APIResult<out T> {
    data class Success<T>(val data: T) : APIResult<T>()
    data class Error(val error: APIError) : APIResult<Nothing>()
    object Loading : APIResult<Nothing>()
}

class UserErrorDialog(private val context: Context) {
    fun showErrorDialog(error: APIError, onDismiss: () -> Unit) {
        // Inflate custom layout
        val inflater = LayoutInflater.from(context)
        val dialogView: View = inflater.inflate(R.layout.dialog_user_error, null)

        // Bind UI components
        val errorTitle = dialogView.findViewById<TextView>(R.id.errorTitle)
        val errorMessage = dialogView.findViewById<TextView>(R.id.errorMessage)
        val dismissButton = dialogView.findViewById<Button>(R.id.dismissButton)

        // Set error message and title
        errorTitle.text = "Hata"
        errorMessage.text = error.userErrorMessage

        // Create dialog
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        // Dismiss button action
        dismissButton.setOnClickListener {
            dialog.dismiss()
            onDismiss()
        }

        dialog.show()
    }
}

class BarberAPIService @Inject constructor(
    private val context: Context,
    private val api: ApiService
) {
    private val networkChecker = NetworkChecker(context)

    suspend fun getBusinesses(): APIResult<List<Business>> {
        if (!networkChecker.isNetworkAvailable()) {
            return APIResult.Error(APIError.UnableToComplete)
        }

        return try {
            val response = api.getBusinesses()
            if (response.isSuccessful) {
                response.body()?.let {
                    APIResult.Success(it)
                } ?: APIResult.Error(APIError.InvalidData)
            } else {
                APIResult.Error(APIError.InvalidResponse)
            }
        } catch (e: Exception) {
            APIResult.Error(APIError.UnableToComplete)
        }
    }

    suspend fun getBusinessServices(id: Int): APIResult<List<Service>> {
        if (!networkChecker.isNetworkAvailable()) {
            return APIResult.Error(APIError.UnableToComplete)
        }

        return try {
            val response = api.getBusinessServices(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    APIResult.Success(it)
                } ?: APIResult.Error(APIError.InvalidData)
            } else {
                APIResult.Error(APIError.InvalidResponse)
            }
        } catch (e: Exception) {
            APIResult.Error(APIError.UnableToComplete)
        }
    }

    suspend fun createReservation(request: ReservationRequest): APIResult<Reservation> {
        if (!networkChecker.isNetworkAvailable()) {
            return APIResult.Error(APIError.UnableToComplete)
        }

        return try {
            val response = api.createReservation(request)
            if (response.isSuccessful) {
                response.body()?.let {
                    APIResult.Success(it)
                } ?: APIResult.Error(APIError.InvalidData)
            } else {
                APIResult.Error(APIError.InvalidResponse)
            }
        } catch (e: Exception) {
            APIResult.Error(APIError.UnableToComplete)
        }
    }
}

class NetworkChecker @Inject constructor(private val context: Context) {
    fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}
