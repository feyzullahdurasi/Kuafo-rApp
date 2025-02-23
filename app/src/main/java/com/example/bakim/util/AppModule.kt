package com.example.bakim.util

import android.content.Context
import com.example.bakim.repository.BakimRepository
import com.example.bakim.service.ApiService
import com.example.bakim.service.BakimAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBakimAPIService(
        @ApplicationContext context: Context,
        apiService: ApiService
    ): BakimAPIService {
        return BakimAPIService(context, apiService)
    }

    @Provides
    @Singleton
    fun provideBakimRepository(
        bakimAPIService: BakimAPIService
    ): BakimRepository {
        return BakimRepository(bakimAPIService)
    }
}