package com.example.kuafrapp.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.collection.LruCache
import com.example.kuafrapp.model.Bakim
import com.example.kuafrapp.model.Business
import com.example.kuafrapp.model.Reservation
import com.example.kuafrapp.model.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.Response
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

// Retrofit Interface
interface ApiService {
    @GET("/bakim")
    suspend fun getBakim(): List<Bakim>

    @GET("businesses")
    suspend fun getBusinesses(): Response<List<Business>>
    
    @GET("businesses/{id}")
    suspend fun getBusinessById(@Path("id") id: Int): Response<Business>
    
    @GET("businesses/{id}/services")
    suspend fun getBusinessServices(@Path("id") id: Int): Response<List<Service>>
    
    @GET("/services")
    suspend fun getAllServices(): Response<List<Service>>
    
    @POST("/reservations")
    suspend fun createReservation(@Body reservation: ReservationRequest): Response<Reservation>
    
    @GET("/users/{id}/reservations")
    suspend fun getUserReservations(@Path("id") userId: Int): Response<List<Reservation>>
}

class ApiResult<T> {
    var data: T? = null
    var error: String? = null
    var isLoading: Boolean = false
}

// Singleton Network Service
object NetworkService {
    private const val BASE_URL = BuildConfig.API_BASE_URL
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) 
                HttpLoggingInterceptor.Level.BODY 
            else 
                HttpLoggingInterceptor.Level.NONE
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ApiService = retrofit.create(ApiService::class.java)
}

// Image Cache
object ImageCache {
    private const val CACHE_SIZE = 20 * 1024 * 1024 // 20MB
    private val cache = LruCache<String, Bitmap>(CACHE_SIZE)

    suspend fun loadImage(url: String): Bitmap? = withContext(Dispatchers.IO) {
        getBitmapFromCache(url) ?: downloadImage(url)?.also { bitmap ->
            cacheBitmap(url, bitmap)
        }
    }

    private fun getBitmapFromCache(url: String): Bitmap? = cache.get(url)
    
    private fun cacheBitmap(url: String, bitmap: Bitmap) {
        cache.put(url, bitmap)
    }

    private suspend fun downloadImage(url: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream = connection.inputStream
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            null
        }
    }
}

// Sample function to fetch Bakim data
suspend fun fetchBakim(): Result<List<Bakim>> {
    return try {
        val response = NetworkService.api.getBakim()
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
