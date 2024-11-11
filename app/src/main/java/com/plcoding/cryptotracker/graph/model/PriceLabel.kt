package com.plcoding.cryptotracker.graph.model

import android.icu.util.CurrencyAmount
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

data class PriceLabel(
    val price: CurrencyAmount,
) {
    fun formatted(): String {
        val formatter =
            NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
                currency = Currency.getInstance("USD")
                val fractionDigits =
                    when {
                        price.number.toDouble() > 1000 -> 0
                        price.number.toDouble() in 2f..1000f -> 2
                        else -> 3
                    }
                maximumFractionDigits = fractionDigits
                minimumFractionDigits = 0
            }
        return formatter.format(price.number)
    }
}
