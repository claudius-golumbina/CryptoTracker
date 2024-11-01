package com.plcoding.cryptotracker.detail.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.plcoding.cryptotracker.detail.presentation.model.CoinDetailAction
import com.plcoding.cryptotracker.overview.ui.component.previewModel
import com.plcoding.cryptotracker.overview.ui.model.CoinListUiState
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme
import com.plcoding.cryptotracker.ui.theme.darkGreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CoinDetailScreen(
    state: CoinListUiState,
    modifier: Modifier = Modifier,
    onAction: (CoinDetailAction) -> Unit = {},
) {
    BackHandler {
        onAction(CoinDetailAction.Back)
    }

    if (state.isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (state.selectedCoin != null) {
        val coin = state.selectedCoin
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
                    icon = com.plcoding.cryptotracker.R.drawable.stock,
                )
                CoinInfoCard(
                    title = "Price",
                    formattedText = coin.price,
                    icon = com.plcoding.cryptotracker.R.drawable.dollar,
                )
                CoinInfoCard(
                    title = "Change",
                    formattedText = coin.changeAmount,
                    icon =
                        if (coin.isNegativeChange) {
                            com.plcoding.cryptotracker.R.drawable.trending_down
                        } else {
                            com.plcoding.cryptotracker.R.drawable.trending
                        },
                    contentColor =
                        if (coin.isNegativeChange) {
                            MaterialTheme.colorScheme.error
                        } else {
                            darkGreen
                        },
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun CoinDetailScreenItemPreview() {
    CryptoTrackerTheme {
        CoinDetailScreen(
            state = CoinListUiState(selectedCoin = previewModel),
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CoinDetailScreenLoadingPreview() {
    CryptoTrackerTheme {
        CoinDetailScreen(state = CoinListUiState(isLoading = true))
    }
}
