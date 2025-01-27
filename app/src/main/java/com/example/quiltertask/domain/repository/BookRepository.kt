package com.example.quiltertask.domain.repository

import com.example.quiltertask.domain.model.Book
import com.example.quiltertask.data.utils.DataError
import com.example.quiltertask.data.utils.Result
import io.reactivex.rxjava3.core.Single

interface BookRepository {
    fun getBooks(): Single<Result<List<Book>, DataError.Network>>
}