package com.example.quiltertask.data.datasource.remote

import com.example.quiltertask.data.datasource.model.response.ApiResponseDTO
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface BookApiService {
    @GET("people/mekBot/books/want-to-read.json")
    fun getBooks(): Observable<ApiResponseDTO>
}