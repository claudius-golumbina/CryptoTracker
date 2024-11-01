package com.plcoding.cryptotracker.overview.data.web.model

import kotlinx.serialization.Serializable

@Serializable
data class CoinListWebEntity(val data: List<CoinWebEntity>)
