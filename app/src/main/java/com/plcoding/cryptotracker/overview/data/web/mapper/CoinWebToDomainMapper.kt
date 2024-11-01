package com.plcoding.cryptotracker.overview.data.web.mapper

import android.icu.util.Currency
import android.icu.util.CurrencyAmount
import com.plcoding.cryptotracker.overview.data.web.model.CoinWebEntity
import com.plcoding.cryptotracker.overview.domain.model.Coin
import com.plcoding.cryptotracker.overview.domain.model.CoinId
import com.plcoding.cryptotracker.overview.domain.model.CoinSymbol
import com.plcoding.cryptotracker.overview.domain.model.Rank
import java.util.Locale

class CoinWebToDomainMapper {
    fun toDomain(item: CoinWebEntity): Coin =
        Coin(
            id = CoinId(item.id),
            rank = Rank(item.rank),
            name = item.name,
            symbol = CoinSymbol(item.symbol),
            marketCap = CurrencyAmount(item.marketCapUsd, Currency.getInstance(Locale.US)),
            price = CurrencyAmount(item.priceUsd, Currency.getInstance(Locale.US)),
            changePercentage = item.changePercent24Hr,
        )
}
