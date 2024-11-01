package com.plcoding.cryptotracker.overview.presentation.model

import android.icu.util.CurrencyAmount
import com.plcoding.cryptotracker.overview.domain.model.CoinId

data class CoinPresentationModel(
    val id: CoinId,
    val name: String,
    val symbol: String,
    val marketCap: CurrencyAmount,
    val price: CurrencyAmount,
    val changePercentage: Double,
    val iconKey: String,
)
