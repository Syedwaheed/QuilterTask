package com.example.quiltertask.di

import android.util.Log
import com.example.quiltertask.BuildConfig
import com.example.quiltertask.data.datasource.remote.BookApiService
import com.example.quiltertask.data.mapper.BookMapper
import com.example.quiltertask.data.repository.BookRepositoryImpl
import com.example.quiltertask.data.repository.ErrorMapperImpl
import com.example.quiltertask.domain.repository.BookRepository
import com.example.quiltertask.domain.repository.ErrorMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://openlibrary.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // RxJava3 adapter
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideBookApiService(retrofit: Retrofit): BookApiService {
        return retrofit.create(BookApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d("API_LOG", message)
        }
        loggingInterceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY // Log request and response body
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30,TimeUnit.SECONDS)
            .readTimeout(30,TimeUnit.SECONDS)
            .writeTimeout(30,TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideBookRepository(
        bookApiService: BookApiService,
        bookMapper: BookMapper,
        errorMapper: ErrorMapper
    ): BookRepository = BookRepositoryImpl(bookApiService, bookMapper, errorMapper)

    @Singleton
    @Provides
    fun provideErrorMapper(): ErrorMapper = ErrorMapperImpl()

}