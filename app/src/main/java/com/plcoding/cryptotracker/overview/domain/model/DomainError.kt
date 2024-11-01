package com.plcoding.cryptotracker.overview.domain.model

import com.plcoding.cryptotracker.core.domain.Error

enum class DomainError : Error {
    TRY_AGAIN,
    NO_INTERNET,
    UNKNOWN_ERROR,
}