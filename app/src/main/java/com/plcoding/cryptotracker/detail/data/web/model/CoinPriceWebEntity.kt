package com.plcoding.cryptotracker.detail.data.web.model

import kotlinx.serialization.Serializable

@Serializable
data class CoinPriceWebEntity(
    val priceUsd: String,
    val time: Long,
)
