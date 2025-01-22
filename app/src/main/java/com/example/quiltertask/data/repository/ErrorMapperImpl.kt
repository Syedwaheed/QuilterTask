package com.example.quiltertask.data.repository

import com.example.quiltertask.domain.repository.ErrorMapper
import com.newapp.composeapplicationstart.data.utils.DataError
import okio.IOException
import retrofit2.HttpException


class ErrorMapperImpl : ErrorMapper{
    override fun mapError(throwable: Throwable): DataError.Network {
        return when(throwable){
            is HttpException ->{
                when(throwable.code()){
                    400 -> DataError.Network.BAD_REQUEST
                    401 -> DataError.Network.UN_AUTHORIZE
                    404 -> DataError.Network.NOT_FOUND
                    408 -> DataError.Network.REQUEST_TIMEOUT
                    500 -> DataError.Network.SERVER_ERROR
                    429 -> DataError.Network.TOO_MANY_REQUEST
                    else -> DataError.Network.UNKNOWN_ERROR
                }
            }
            is IOException -> DataError.Network.NO_INTERNET
            else -> DataError.Network.UNKNOWN_ERROR
        }
    }
}