package com.grig.danish.di

import com.grig.core.network.ResultCallAdapterFactory
import com.grig.danish.data.DanishService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    private val json = Json { ignoreUnknownKeys = true }

    @Singleton
    @Provides
    @Named("Danish")
    fun provideOkHttpClientBuilder() = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)

    @OptIn(ExperimentalSerializationApi::class)
    @Singleton
    @Provides
    fun provideRetrofit(
        @Named("Danish") okHttpClientBuilder: OkHttpClient.Builder
    ): Retrofit = Retrofit.Builder()
        .client(okHttpClientBuilder.build())
        .baseUrl("https://ordnet.dk/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .addCallAdapterFactory(ResultCallAdapterFactory())
        .build()

    @Singleton
    @Provides
    fun provideDanishService(
        retrofit: Retrofit
    ): DanishService = retrofit.create(DanishService::class.java)
}
