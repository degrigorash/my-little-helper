package com.grig.myanimelist.di

import com.grig.myanimelist.data.AuthorizationInterceptor
import com.grig.myanimelist.data.MalAuthService
import com.grig.myanimelist.data.MalService
import com.grig.myanimelist.data.TokenManager
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
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    private val json = Json

    @Singleton
    @Provides
    fun provideOkHttpClient(
        tokenManager: TokenManager
    ) = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(AuthorizationInterceptor(tokenManager))
        .build()

    @OptIn(ExperimentalSerializationApi::class)
    @Singleton
    @Provides
    @Named("Oauth2")
    fun provideAuthRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://myanimelist.net/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @OptIn(ExperimentalSerializationApi::class)
    @Singleton
    @Provides
    @Named("Mal")
    fun provideMalRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://api.myanimelist.net/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Singleton
    @Provides
    fun provideMalAuthService(
        @Named("Oauth2") retrofit: Retrofit
    ): MalAuthService = retrofit.create(MalAuthService::class.java)

    @Singleton
    @Provides
    fun provideMalService(
        @Named("Mal") retrofit: Retrofit
    ): MalService = retrofit.create(MalService::class.java)
}