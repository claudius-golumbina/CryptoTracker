package com.plcoding.cryptotracker.overview.ui.mapper

import com.plcoding.cryptotracker.overview.presentation.model.CoinListPresentationState
import com.plcoding.cryptotracker.overview.ui.model.CoinListUiState

class CoinListStatePresentationToUiMapper(
    private val coinPresentationToUiMapper: CoinPresentationToUiMapper,
) {
    fun toUi(coinListPresentationState: CoinListPresentationState): CoinListUiState =
        CoinListUiState(
            isLoading = coinListPresentationState.isLoading,
            isRefreshing = coinListPresentationState.isRefreshing,
            coins =
                coinListPresentationState.coins.map {
                    coinPresentationToUiMapper.toUi(it)
                },
            selectedCoin =
                coinListPresentationState.selectedCoin?.let {
                    coinPresentationToUiMapper.toUi(it)
                },
        )
}
