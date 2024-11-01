package com.plcoding.cryptotracker.overview.presentation.mapper

import com.plcoding.cryptotracker.overview.domain.model.Coin
import com.plcoding.cryptotracker.overview.presentation.model.CoinPresentationModel

class CoinDomainToPresentationMapper {

    fun toPresentation(coin: Coin): CoinPresentationModel {
        return CoinPresentationModel(
            id = coin.id,
            name = coin.name,
            symbol = coin.symbol.value,
            marketCap = coin.marketCap,
            price = coin.price,
            changePercentage = coin.changePercentage,
            iconKey = coin.symbol.value
        )
    }
}