package com.plcoding.cryptotracker.overview.ui.model

import androidx.annotation.DrawableRes
import com.plcoding.cryptotracker.overview.domain.model.CoinId

data class CoinUiModel(
    val id: CoinId,
    val name: String,
    val symbol: String,
    val marketCap: String,
    val price: String,
    val changePercentage: String,
    val isNegativeChange: Boolean,
    val changeAmount: String,
    @DrawableRes val iconResId: Int
)
