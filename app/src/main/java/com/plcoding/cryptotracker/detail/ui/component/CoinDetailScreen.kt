package com.plcoding.cryptotracker.detail.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.cryptotracker.R
import com.plcoding.cryptotracker.detail.presentation.model.CoinDetailAction
import com.plcoding.cryptotracker.detail.ui.model.CoinDetailUiState
import com.plcoding.cryptotracker.graph.component.LineChart
import com.plcoding.cryptotracker.graph.model.ChartStyle
import com.plcoding.cryptotracker.graph.model.DataPoint
import com.plcoding.cryptotracker.overview.ui.component.previewModel
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme
import com.plcoding.cryptotracker.ui.theme.darkGreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CoinDetailScreen(
    state: CoinDetailUiState,
    modifier: Modifier = Modifier,
    onAction: (CoinDetailAction) -> Unit = {},
) {
    //BackHandler {
    //    onAction(CoinDetailAction.Back)
    //}

    val coin = state.coinDetail
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(coin.iconResId),
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = coin.name,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = coin.symbol,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.onSurface,
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            CoinInfoCard(
                title = "Market Cap",
                formattedText = coin.marketCap,
                icon = R.drawable.stock,
            )
            CoinInfoCard(
                title = "Price",
                formattedText = coin.price,
                icon = R.drawable.dollar,
            )
            CoinInfoCard(
                title = "Change",
                formattedText = coin.changeAmount,
                icon =
                    if (coin.isNegativeChange) {
                        R.drawable.trending_down
                    } else {
                        R.drawable.trending
                    },
                contentColor =
                    if (coin.isNegativeChange) {
                        MaterialTheme.colorScheme.error
                    } else {
                        darkGreen
                    },
            )
        }

        // AnimatedVisibility(visible = state.isLoading) {
        if (state.isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        } else {
            var selectedDataPoint by remember { mutableStateOf<DataPoint?>(null) }
            var xLabelWidth by remember { mutableFloatStateOf(0f) }
            var totalChartWidth by remember { mutableFloatStateOf(0f) }
            val numberOfVisibleDataPoints =
                if (xLabelWidth > 0f) {
                    ((totalChartWidth - 2.5 * xLabelWidth) / xLabelWidth).toInt()
                } else {
                    0
                }
            val startIndex =
                (state.coinHistory.lastIndex - numberOfVisibleDataPoints).coerceAtLeast(0)

            LineChart(
                dataPoints = state.coinHistory,
                currency = state.currency,
                style =
                    ChartStyle(
                        chartLineColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.secondary,
                        selectedColor = MaterialTheme.colorScheme.primary,
                        helperLinesThicknessPx = 4f,
                        axisLinesThicknessPx = 5f,
                        labelFontSize = 14.sp,
                        minYLabelSpacing = 25.dp,
                        verticalPadding = 8.dp,
                        horizontalPadding = 8.dp,
                        xLabelSpacing = 8.dp,
                    ),
                visibleDataPointIndices = startIndex..state.coinHistory.lastIndex,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(16 / 10f)
                        .onSizeChanged { totalChartWidth = it.width.toFloat() },
                selectedDataPoint = selectedDataPoint,
                onSelectedDataPointChange = { selectedDataPoint = it },
                onXLabelWidthChange = { xLabelWidth = it },
                showHelperLines = true,
            )
        }
    }
}

@PreviewLightDark
@Composable
fun CoinDetailScreenItemPreview() {
    CryptoTrackerTheme {
        CoinDetailScreen(
            state = CoinDetailUiState(coinDetail = previewModel),
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CoinDetailScreenLoadingPreview() {
    CryptoTrackerTheme {
        CoinDetailScreen(state = CoinDetailUiState(isLoading = true))
    }
}
