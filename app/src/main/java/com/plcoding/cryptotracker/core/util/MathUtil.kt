package com.plcoding.cryptotracker.core.util

import android.icu.util.CurrencyAmount

operator fun CurrencyAmount.times(other: Double): CurrencyAmount = CurrencyAmount(number.toDouble() * other, currency)

operator fun CurrencyAmount.div(other: Double): CurrencyAmount = CurrencyAmount(number.toDouble() / other, currency)
