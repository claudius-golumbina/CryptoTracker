package com.plcoding.cryptotracker.overview.presentation.model

import android.icu.util.Currency
import android.icu.util.CurrencyAmount
import com.plcoding.cryptotracker.overview.domain.model.CoinId
import java.util.Locale

data class CoinPresentationModel(
    val id: CoinId,
    val name: String,
    val symbol: String,
    val marketCap: CurrencyAmount,
    val price: CurrencyAmount,
    val changePercentage: Double,
    val iconKey: String,
) {
    companion object {
        val EMPTY =
            CoinPresentationModel(
                id = CoinId(""),
                name = "",
                symbol = "",
                marketCap = CurrencyAmount(0f, Currency.getInstance(Locale.US)),
                price = CurrencyAmount(0f, Currency.getInstance(Locale.US)),
                changePercentage = 0.0,
                iconKey = "",
            )
    }
}
