package com.plcoding.cryptotracker.detail.presentation.mapper

import com.plcoding.cryptotracker.detail.domain.model.CoinPrice
import com.plcoding.cryptotracker.detail.presentation.model.CoinPricePresentationModel

class CoinPriceHistoryDomainToPresentationMapper {
    fun toPresentation(coinPrice: CoinPrice): CoinPricePresentationModel =
        CoinPricePresentationModel(
            price = coinPrice.price,
            time = coinPrice.time,
        )
}
