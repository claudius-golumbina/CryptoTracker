package com.plcoding.cryptotracker.overview.domain.model

import android.icu.util.CurrencyAmount

data class Coin(
    val id: CoinId,
    val rank: Rank,
    val name: String,
    val symbol: CoinSymbol,
    val marketCap: CurrencyAmount,
    val price: CurrencyAmount,
    val changePercentage: Double,
)
