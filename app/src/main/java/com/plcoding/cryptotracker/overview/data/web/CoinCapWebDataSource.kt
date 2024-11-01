package com.plcoding.cryptotracker.overview.data.web

import com.plcoding.cryptotracker.core.data.network.NetworkError
import com.plcoding.cryptotracker.core.data.network.constructUrl
import com.plcoding.cryptotracker.core.data.network.safeCall
import com.plcoding.cryptotracker.core.domain.Result
import com.plcoding.cryptotracker.detail.data.web.model.CoinHistoryWebEntity
import com.plcoding.cryptotracker.overview.data.RemoteCoinDataSource
import com.plcoding.cryptotracker.overview.data.web.model.CoinListWebEntity
import com.plcoding.cryptotracker.overview.domain.model.CoinId
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import java.time.ZoneId
import java.time.ZonedDateTime

class CoinCapWebDataSource(
    private val httpClient: HttpClient,
) : RemoteCoinDataSource {
    override suspend fun getCoins(): Result<CoinListWebEntity, NetworkError> =
        safeCall<CoinListWebEntity> {
            httpClient.get(urlString = constructUrl("/assets"))
        }

    override suspend fun getCoinHistory(
        coinId: CoinId,
        start: ZonedDateTime,
        end: ZonedDateTime,
    ): Result<CoinHistoryWebEntity, NetworkError> =
        safeCall<CoinHistoryWebEntity> {
            httpClient.get(urlString = constructUrl("/assets/${coinId.id}/history")) {
                parameter("interval", "h6")
                parameter("start", start.toMillis())
                parameter("end", end.toMillis())
            }
        }

    private fun ZonedDateTime.toMillis() = withZoneSameInstant(ZoneId.of("UTC")).toInstant().toEpochMilli()
}
