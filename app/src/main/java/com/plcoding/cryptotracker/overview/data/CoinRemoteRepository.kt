package com.plcoding.cryptotracker.overview.data

import com.plcoding.cryptotracker.core.data.network.NetworkError
import com.plcoding.cryptotracker.core.domain.Result
import com.plcoding.cryptotracker.core.domain.mapError
import com.plcoding.cryptotracker.core.domain.mapResult
import com.plcoding.cryptotracker.detail.data.web.mapper.CoinPriceWebToDomainMapper
import com.plcoding.cryptotracker.detail.domain.model.CoinPrice
import com.plcoding.cryptotracker.overview.data.web.mapper.CoinWebToDomainMapper
import com.plcoding.cryptotracker.overview.domain.CoinRepository
import com.plcoding.cryptotracker.overview.domain.model.Coin
import com.plcoding.cryptotracker.overview.domain.model.CoinId
import com.plcoding.cryptotracker.overview.domain.model.DomainError
import java.time.ZonedDateTime

class CoinRemoteRepository(
    private val dataSource: RemoteCoinDataSource,
    private val coinMapper: CoinWebToDomainMapper,
    private val priceMapper: CoinPriceWebToDomainMapper,
) : CoinRepository {
    override suspend fun getCoins(): Result<List<Coin>, DomainError> =
        dataSource
            .getCoins()
            .mapResult { response -> response.data }
            .mapResult { list ->
                list.map { item ->
                    coinMapper.toDomain(item)
                }
            }.mapError { error -> mapNetworkError(error) }

    override suspend fun getCoinHistory(
        coinId: CoinId,
        start: ZonedDateTime,
        end: ZonedDateTime,
    ): Result<List<CoinPrice>, DomainError> =
        dataSource
            .getCoinHistory(coinId, start, end)
            .mapResult { response -> response.data }
            .mapResult { list ->
                list.map { item ->
                    priceMapper.toDomain(item)
                }
            }.mapError { error -> mapNetworkError(error) }

    private fun mapNetworkError(error: NetworkError): DomainError =
        when (error) {
            NetworkError.REQUEST_TIMEOUT -> DomainError.TRY_AGAIN
            NetworkError.TOO_MANY_REQUESTS -> DomainError.TRY_AGAIN
            NetworkError.NO_INTERNET -> DomainError.NO_INTERNET
            NetworkError.SERVER_ERROR -> DomainError.TRY_AGAIN
            NetworkError.SERIALIZATION_ERROR -> DomainError.TRY_AGAIN
            NetworkError.UNKNOWN_ERROR -> DomainError.UNKNOWN_ERROR
        }
}
