package com.plcoding.cryptotracker.detail.presentation.model

import com.plcoding.cryptotracker.overview.presentation.model.CoinPresentationModel

data class CoinDetailPresentationState(
    val isLoading: Boolean = false,
    val coinDetail: CoinPresentationModel = CoinPresentationModel.EMPTY,
    val coinHistory: List<CoinPricePresentationModel> = emptyList()
)