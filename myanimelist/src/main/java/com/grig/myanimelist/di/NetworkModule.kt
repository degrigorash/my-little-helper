package com.grig.myanimelist.di

import com.grig.core.network.ResultCallAdapterFactory
import com.grig.myanimelist.data.JikanService
import com.grig.myanimelist.data.MalAuthService
import com.grig.myanimelist.data.MalService
import com.grig.myanimelist.data.UserManager
import com.grig.myanimelist.data.setup.AuthorizationInterceptor
import com.grig.myanimelist.data.setup.JikanRetryInterceptor
import com.grig.myanimelist.data.setup.TenraiFallbackInterceptor
import com.grig.myanimelist.data.setup.TokenAuthenticator
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

    private val json = Json {
        ignoreUnknownKeys = true
        // Fall back to a property's default when the API returns an enum value
        // we don't model (e.g. media_type "tv_special"/"cm"/"pv") instead of
        // throwing and failing the whole response.
        coerceInputValues = true
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
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
        .addCallAdapterFactory(ResultCallAdapterFactory())
        .build()

    @OptIn(ExperimentalSerializationApi::class)
    @Singleton
    @Provides
    @Named("Mal")
    fun provideMalRetrofit(
        okHttpClient: OkHttpClient,
        userManager: UserManager,
        malAuthService: MalAuthService
    ): Retrofit = Retrofit.Builder()
        .client(
            okHttpClient.newBuilder()
                .addInterceptor(AuthorizationInterceptor(userManager))
                .authenticator(TokenAuthenticator(userManager, malAuthService))
                .build()
        )
        .baseUrl("https://api.myanimelist.net/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .addCallAdapterFactory(ResultCallAdapterFactory())
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

    @OptIn(ExperimentalSerializationApi::class)
    @Singleton
    @Provides
    @Named("Jikan")
    fun provideJikanRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .client(
            // Retry sits outside the fallback, so each retry round attempts
            // Tenrai first and replays against Jikan on failure.
            okHttpClient.newBuilder()
                .addInterceptor(JikanRetryInterceptor())
                .addInterceptor(TenraiFallbackInterceptor())
                .build()
        )
        .baseUrl("https://api.tenrai.org/v1/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .addCallAdapterFactory(ResultCallAdapterFactory())
        .build()

    @Singleton
    @Provides
    fun provideJikanService(
        @Named("Jikan") retrofit: Retrofit
    ): JikanService = retrofit.create(JikanService::class.java)
}