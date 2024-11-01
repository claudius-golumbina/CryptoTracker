package com.plcoding.cryptotracker.overview.domain

import com.plcoding.cryptotracker.core.domain.Result
import com.plcoding.cryptotracker.detail.domain.model.CoinPrice
import com.plcoding.cryptotracker.overview.domain.model.Coin
import com.plcoding.cryptotracker.overview.domain.model.CoinId
import com.plcoding.cryptotracker.overview.domain.model.DomainError
import java.time.ZonedDateTime

interface CoinRepository {
    suspend fun getCoins(): Result<List<Coin>, DomainError>

    suspend fun getCoinHistory(
        coinId: CoinId,
        start: ZonedDateTime,
        end: ZonedDateTime,
    ): Result<List<CoinPrice>, DomainError>
}
