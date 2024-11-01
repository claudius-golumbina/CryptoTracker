package com.plcoding.cryptotracker.overview.domain.usecase

import com.plcoding.cryptotracker.overview.domain.CoinRepository

class GetCoinsUseCase(private val coinRepository: CoinRepository) {
    suspend operator fun invoke() = coinRepository.getCoins()
}