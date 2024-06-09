package com.example.storyappsubmission.di

import com.example.storyappsubmission.BuildConfig
import com.example.storyappsubmission.data.remote.TokenManager
import com.example.storyappsubmission.data.remote.retrofit.StoryService
import com.example.storyappsubmission.data.repository.StoryRepositoryImpl
import com.example.storyappsubmission.domain.repository.StoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StoryModule {
    @Provides
    @Singleton
    fun provideToken(pref: TokenManager): String {
        return runBlocking { pref.getToken().first() }
    }

    @Provides
    @Singleton
    fun provideStoryServices(token: String): StoryService {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(requestHeaders)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(StoryService::class.java)
    }

    @Provides
    @Singleton
    fun providesStoryRepository(storyService: StoryService): StoryRepository {
        return StoryRepositoryImpl(storyService)
    }

}