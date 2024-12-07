package com.example.kuafrapp.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.collection.LruCache
import com.example.kuafrapp.model.Bakim
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

// Retrofit Interface
interface ApiService {
    @GET("/bakim")
    suspend fun getBakim(): List<Bakim>
}

// Singleton Network Service
object NetworkService {
    private const val BASE_URL = "http://localhost:3000"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}



// Image Cache
object ImageCache {
    private val cache = LruCache<String, Bitmap>(20)

    fun getBitmapFromCache(url: String): Bitmap? {
        return cache.get(url)
    }

    fun saveBitmapToCache(url: String, bitmap: Bitmap) {
        cache.put(url, bitmap)
    }
}

// Image Downloader
suspend fun downloadImage(urlString: String): Bitmap? {
    // Check cache first
    ImageCache.getBitmapFromCache(urlString)?.let {
        return it
    }

    return withContext(Dispatchers.IO) {
        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(inputStream)
            ImageCache.saveBitmapToCache(urlString, bitmap)
            bitmap
        } catch (e: IOException) {
            e.printStackTrace()
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
