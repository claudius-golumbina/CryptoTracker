package com.plcoding.cryptotracker.detail.data.web.model

import kotlinx.serialization.Serializable

@Serializable
data class CoinHistoryWebEntity(
    val data: List<CoinPriceWebEntity>
)
