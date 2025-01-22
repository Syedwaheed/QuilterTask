package com.example.quiltertask.data.mapper

import com.example.quiltertask.data.datasource.model.response.ApiResponseDTO
import com.example.quiltertask.domain.model.Book
import com.example.quiltertask.data.utils.UrlConstant
import javax.inject.Inject

class BookMapper @Inject constructor() {
    fun mapApiResponseDTO(work: ApiResponseDTO.ReadingLogEntry.Work): Book {
        return Book(
            title = work.title ?: "",
            author = work.authorNames?.joinToString(",") ?: "",
            coverID = work.coverId?.let { coverId ->
                UrlConstant.getCoverUrl(coverId)
            } ?: ""
        )
    }
}