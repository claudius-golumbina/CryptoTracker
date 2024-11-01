package com.plcoding.cryptotracker.detail.domain.usecase

import com.plcoding.cryptotracker.overview.domain.CoinRepository
import com.plcoding.cryptotracker.overview.domain.model.CoinId
import java.time.Duration
import java.time.ZonedDateTime

class GetPriceHistoryUseCase(
    private val coinRepository: CoinRepository,
) {
    suspend operator fun invoke(coinId: CoinId) =
        coinRepository.getCoinHistory(
            coinId = coinId,
            start = ZonedDateTime.now() - Duration.ofDays(5), // .minusDays(5)
            end = ZonedDateTime.now(),
        )
}
