package com.plcoding.cryptotracker.detail.presentation.model

sealed interface CoinDetailAction {
    data object Back : CoinDetailAction
}