package com.plcoding.cryptotracker.overview.data

import com.plcoding.cryptotracker.core.data.network.NetworkError
import com.plcoding.cryptotracker.core.domain.Result
import com.plcoding.cryptotracker.detail.data.web.model.CoinHistoryWebEntity
import com.plcoding.cryptotracker.overview.data.web.model.CoinListWebEntity
import com.plcoding.cryptotracker.overview.domain.model.CoinId
import java.time.ZonedDateTime

interface RemoteCoinDataSource {
    suspend fun getCoins(): Result<CoinListWebEntity, NetworkError>
    suspend fun getCoinHistory(
        coinId: CoinId,
        start: ZonedDateTime,
        end: ZonedDateTime
    ): Result<CoinHistoryWebEntity, NetworkError>
}