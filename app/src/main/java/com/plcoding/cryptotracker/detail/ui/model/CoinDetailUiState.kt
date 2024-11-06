package com.plcoding.cryptotracker.detail.ui.model

import android.icu.util.Currency
import com.plcoding.cryptotracker.graph.model.DataPoint
import com.plcoding.cryptotracker.overview.ui.model.CoinUiModel
import java.util.Locale

data class CoinDetailUiState(
    val isLoading: Boolean = false,
    val coinDetail: CoinUiModel = CoinUiModel.EMPTY,
    val currency: Currency = Currency.getInstance(Locale.getDefault()),
    val coinHistory: List<DataPoint> = emptyList()
)
