package com.plcoding.cryptotracker.core.data.network

import com.plcoding.cryptotracker.core.domain.Error

enum class NetworkError : Error {
    REQUEST_TIMEOUT,
    TOO_MANY_REQUESTS,
    NO_INTERNET,
    SERVER_ERROR,
    SERIALIZATION_ERROR,
    UNKNOWN_ERROR,
}