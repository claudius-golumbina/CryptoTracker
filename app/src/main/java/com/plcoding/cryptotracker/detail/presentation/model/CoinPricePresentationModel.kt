package com.plcoding.cryptotracker.detail.presentation.model

import android.icu.util.CurrencyAmount
import java.time.ZonedDateTime

data class CoinPricePresentationModel(
    val price: CurrencyAmount,
    val time: ZonedDateTime,
)
