package com.plcoding.cryptotracker.overview.ui.mapper

import android.icu.text.NumberFormat
import android.icu.util.CurrencyAmount
import com.plcoding.cryptotracker.core.ui.getDrawableIdForCoin
import com.plcoding.cryptotracker.core.util.div
import com.plcoding.cryptotracker.core.util.times
import com.plcoding.cryptotracker.detail.presentation.model.CoinPricePresentationModel
import com.plcoding.cryptotracker.graph.model.DataPoint
import com.plcoding.cryptotracker.overview.presentation.model.CoinPresentationModel
import com.plcoding.cryptotracker.overview.ui.model.CoinUiModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.absoluteValue

class CoinPresentationToUiMapper {
    fun toUi(presentation: CoinPresentationModel): CoinUiModel {
        val changeDecimal = presentation.changePercentage / 100.0
        return CoinUiModel(
            id = presentation.id,
            name = presentation.name,
            symbol = presentation.symbol,
            marketCap = presentation.marketCap.format(),
            price = presentation.price.format(),
            changePercentage = changeDecimal.absoluteValue.formatPercentage(),
            isNegativeChange = presentation.changePercentage < 0,
            changeAmount =
                (
                    presentation.price * changeDecimal /
                        (1 - changeDecimal)
                ).format(),
            iconResId = getDrawableIdForCoin(presentation.iconKey),
        )
    }

    fun toUi(
        presentation: CoinPricePresentationModel,
        index: Int,
    ): DataPoint =
        DataPoint(
            x = index.toFloat(),
            y = presentation.price.number.toFloat(),
            xLabel = presentation.time.format(),
        )

    private fun CurrencyAmount.format(): String {
        val locale = Locale.getDefault()
        val numberFormat =
            NumberFormat.getCurrencyInstance(locale).apply {
                minimumFractionDigits = 2
                maximumFractionDigits = 2
            }
        return numberFormat.format(number)
    }

    private fun Double.formatPercentage(): String {
        val locale = Locale.getDefault()
        val numberFormat = NumberFormat.getPercentInstance(locale)
        numberFormat.minimumFractionDigits = 2
        numberFormat.maximumFractionDigits = 2
        return numberFormat.format(this)
    }

    private fun ZonedDateTime.format(): String = DateTimeFormatter.ofPattern("ha\nM/d").format(this)
}
