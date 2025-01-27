package com.example.quiltertask.domain.usecase

import com.example.quiltertask.domain.model.Book
import com.example.quiltertask.domain.repository.BookRepository
import com.example.quiltertask.data.utils.DataError
import com.example.quiltertask.data.utils.Result
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetBookUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    operator fun invoke(): Single<Result<List<Book>, DataError.Network>> {
        return bookRepository.getBooks()
    }
}