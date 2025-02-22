package com.example.bakim.service

import android.content.Context
import android.graphics.Bitmap
import androidx.collection.LruCache
import com.bumptech.glide.Glide
import com.example.bakim.model.Bakim
import com.example.bakim.model.Business
import com.example.bakim.model.Reservation
import com.example.bakim.model.ReservationRequest
import com.example.bakim.model.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

interface ApiService {
    @GET("/bakim")
    suspend fun getBakim(): Response<List<Bakim>>

    @GET("businesses")
    suspend fun getBusinesses(): Response<List<Business>>

    @GET("businesses/{id}")
    suspend fun getBusinessById(@Path("id") id: Int): Response<Business>

    @GET("businesses/{id}/services")
    suspend fun getBusinessServices(@Path("id") id: Int): Response<List<Service>>

    @GET("/services")
    suspend fun getAllServices(): Response<List<Service>>

    @GET("services/{serviceId}")
    suspend fun getServiceDetails(
        @Path("serviceId") serviceId: Int,
        @Query("businessId") businessId: Int
    ): Response<Service>

    @GET("services/search")
    suspend fun searchServices(@Query("query") query: String): Response<List<Service>>

    @POST("/reservations")
    suspend fun createReservation(@Body reservation: ReservationRequest): Response<Reservation>

    @GET("/users/{id}/reservations")
    suspend fun getUserReservations(@Path("id") userId: Int): Response<List<Reservation>>
}
/*
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG)
                    HttpLoggingInterceptor.Level.BODY
                else
                    HttpLoggingInterceptor.Level.NONE
            })
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Accept", "application/json")
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
/*
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }*/

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}*/

@Singleton
class ImageCacheService @Inject constructor(
    private val context: Context
) {
    private val cache = LruCache<String, Bitmap>(20 * 1024 * 1024)

    suspend fun loadImage(url: String): Bitmap? = withContext(Dispatchers.IO) {
        cache.get(url) ?: downloadImage(url)?.also { bitmap ->
            cache.put(url, bitmap)
        }
    }

    private suspend fun downloadImage(url: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            Glide.with(context)
                .asBitmap()
                .load(url)
                .submit()
                .get()
        } catch (e: Exception) {
            null
        }
    }
}
/*
@Module
@InstallIn(SingletonComponent::class)
object ImageModule {

    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    @Provides
    @Singleton
    fun provideImageCacheService(
        @ApplicationContext context: Context,
        scope: CoroutineScope
    ): ImageCacheService {
        return ImageCacheService(context, scope)
    }
}*/