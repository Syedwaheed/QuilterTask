package com.example.quiltertask.domain.repository

import com.example.quiltertask.domain.model.Book
import com.example.quiltertask.data.utils.DataError
import com.example.quiltertask.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getBooks(): Flow<Result<List<Book>, DataError.Network>>
}