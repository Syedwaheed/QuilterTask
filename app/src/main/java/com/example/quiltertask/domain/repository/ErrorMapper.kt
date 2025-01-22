package com.example.quiltertask.domain.repository

import com.newapp.composeapplicationstart.data.utils.DataError

interface ErrorMapper {
    fun mapError(throwable: Throwable): DataError.Network
}