package com.plcoding.cryptotracker.overview.data.web.model

import kotlinx.serialization.Serializable

@Serializable
data class CoinWebEntity(
    val id: String,
    val rank: Int,
    val name: String,
    val symbol: String,
    val marketCapUsd: Double,
    val priceUsd: Double,
    val changePercent24Hr: Double
)
