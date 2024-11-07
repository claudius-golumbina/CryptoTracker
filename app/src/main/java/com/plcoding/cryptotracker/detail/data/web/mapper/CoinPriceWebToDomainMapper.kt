package com.plcoding.cryptotracker.detail.data.web.mapper

import android.icu.util.Currency
import android.icu.util.CurrencyAmount
import com.plcoding.cryptotracker.detail.data.web.model.CoinPriceWebEntity
import com.plcoding.cryptotracker.detail.domain.model.CoinPrice
import java.time.Instant
import java.time.ZoneId
import java.util.Locale

class CoinPriceWebToDomainMapper {
    fun toDomain(entity: CoinPriceWebEntity) =
        CoinPrice(
            price = CurrencyAmount(entity.priceUsd.toDouble(), Currency.getInstance(Locale.US)),
            time = Instant.ofEpochMilli(entity.time).atZone(ZoneId.systemDefault()),
        )
}
