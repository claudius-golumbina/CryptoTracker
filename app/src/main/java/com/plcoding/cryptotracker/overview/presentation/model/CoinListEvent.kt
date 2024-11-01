package com.plcoding.cryptotracker.overview.presentation.model

import com.plcoding.cryptotracker.overview.domain.model.DomainError

sealed interface CoinListEvent {
    data class Error(val error: DomainError) : CoinListEvent
}