package com.plcoding.cryptotracker.detail.domain.model

import android.icu.util.CurrencyAmount
import java.time.ZonedDateTime

data class CoinPrice(
    val price: CurrencyAmount,
    val time: ZonedDateTime,
)
