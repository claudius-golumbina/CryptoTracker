package com.plcoding.cryptotracker.overview.presentation.model

data class CoinListPresentationState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val coins: List<CoinPresentationModel> = emptyList(),
    val selectedCoin: CoinPresentationModel? = null,
)
