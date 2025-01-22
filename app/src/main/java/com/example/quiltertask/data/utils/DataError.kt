package com.example.quiltertask.data.utils

sealed interface DataError : Error {
    enum class Network: DataError {
        REQUEST_TIMEOUT,
        SERVER_ERROR,
        NO_INTERNET,
        TOO_MANY_REQUEST,
        SERIALIZATION,
        PAYLOAD_TOO_LARGE,
        UNKNOWN_ERROR,
        BAD_REQUEST,
        UN_AUTHORIZE,
        NOT_FOUND
    }
}