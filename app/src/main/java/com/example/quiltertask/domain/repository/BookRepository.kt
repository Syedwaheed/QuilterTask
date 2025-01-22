package com.example.quiltertask.domain.repository

import com.example.quiltertask.domain.model.Book
import com.newapp.composeapplicationstart.data.utils.DataError
import com.newapp.composeapplicationstart.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getBooks(): Flow<Result<List<Book>, DataError.Network>>
}