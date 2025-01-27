package com.example.quiltertask.data.repository

import com.example.quiltertask.data.datasource.remote.BookApiService
import com.example.quiltertask.data.mapper.BookMapper
import com.example.quiltertask.data.utils.DataError
import com.example.quiltertask.data.utils.Result
import com.example.quiltertask.domain.model.Book
import com.example.quiltertask.domain.repository.BookRepository
import com.example.quiltertask.domain.repository.ErrorMapper
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val api: BookApiService,
    private val mapper: BookMapper,
    private val errorMapper: ErrorMapper
) : BookRepository {
    override fun getBooks(): Single<Result<List<Book>, DataError.Network>> {
        return api.getBooks()
            .map<Result<List<Book>, DataError.Network>> { response ->
                val mappedData: List<Book> = response.readingLogEntries
                    ?.mapNotNull { entry ->
                        entry?.work?.let { mapper.mapApiResponseDTO(it) }
                    }
                    .orEmpty()
                Result.Success(mappedData)
            }.onErrorReturn { throwable ->
                Result.Error(errorMapper.mapError(throwable))
            }
    }

}