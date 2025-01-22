package com.example.quiltertask.domain.usecase

import com.example.quiltertask.domain.model.Book
import com.example.quiltertask.domain.repository.BookRepository
import com.example.quiltertask.data.utils.DataError
import com.example.quiltertask.data.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    operator fun invoke(): Flow<Result<List<Book>, DataError.Network>> {
        return bookRepository.getBooks()
    }
}