package com.example.quiltertask.domain.repository

import com.example.quiltertask.data.utils.DataError

interface ErrorMapper {
    fun mapError(throwable: Throwable): DataError.Network
}