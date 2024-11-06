package com.plcoding.cryptotracker.detail.ui.mapper

import com.plcoding.cryptotracker.detail.presentation.model.CoinDetailPresentationState
import com.plcoding.cryptotracker.detail.ui.model.CoinDetailUiState
import com.plcoding.cryptotracker.overview.ui.mapper.CoinPresentationToUiMapper

class CoinDetailPresentationStateToUiMapper(
    private val coinPresentationToUiMapper: CoinPresentationToUiMapper,
) {
    fun toUi(coinDetailStatePresentation: CoinDetailPresentationState): CoinDetailUiState =
        CoinDetailUiState(
            isLoading = coinDetailStatePresentation.isLoading,
            coinDetail =
                coinDetailStatePresentation.coinDetail.let {
                    coinPresentationToUiMapper.toUi(it)
                },
            coinHistory =
                coinDetailStatePresentation.coinHistory.mapIndexed { index, it ->
                    coinPresentationToUiMapper.toUi(it, index)
                },
        )
}
