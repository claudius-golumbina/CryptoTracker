package com.plcoding.cryptotracker.overview.presentation.model

import com.plcoding.cryptotracker.overview.domain.model.CoinId

sealed interface CoinListAction {
    data class OnCoinClick(
        val coinId: CoinId,
    ) : CoinListAction

    data object OnRefresh : CoinListAction
}
