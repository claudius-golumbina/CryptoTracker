package com.plcoding.cryptotracker.overview.ui.model

data class CoinListUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val coins: List<CoinUiModel> = emptyList(),
    val selectedCoin: CoinUiModel? = null,
)