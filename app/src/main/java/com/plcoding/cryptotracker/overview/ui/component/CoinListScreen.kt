package com.plcoding.cryptotracker.overview.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.plcoding.cryptotracker.overview.presentation.model.CoinListAction
import com.plcoding.cryptotracker.overview.presentation.model.CoinListEvent
import com.plcoding.cryptotracker.overview.ui.model.CoinListUiState
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinListScreen(
    coinListUiState: CoinListUiState,
    listState: LazyListState = rememberLazyListState(),
    events: Flow<CoinListEvent>,
    modifier: Modifier = Modifier,
    onAction: (CoinListAction) -> Unit = {},
) {
    if (coinListUiState.isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        val pullToRefreshState = rememberPullToRefreshState()
        PullToRefreshBox(
            state = pullToRefreshState,
            isRefreshing = coinListUiState.isRefreshing,
            onRefresh = { onAction(CoinListAction.OnRefresh) },
        ) {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                state = listState,
            ) {
                items(coinListUiState.coins) { coin ->
                    CoinListItem(
                        coinUiModel = coin,
                        onClick = { onAction(CoinListAction.OnCoinClick(coin.id)) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    HorizontalDivider()
                }
                item {
                    Spacer(modifier = Modifier.height(96.dp))
                }
            }

            FloatingActionButton(
                onClick = {
                    onAction(CoinListAction.OnRefresh)
                },
                modifier =
                    Modifier
                        .padding(horizontal = 16.dp, vertical = 32.dp)
                        .align(Alignment.BottomEnd),
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CoinListScreenLoadingPreview() {
    CryptoTrackerTheme {
        CoinListScreen(coinListUiState = CoinListUiState(isLoading = true), events = emptyFlow())
    }
}

@Preview(showBackground = true)
@Composable
fun CoinListScreenItemsPreview() {
    CryptoTrackerTheme {
        CoinListScreen(
            coinListUiState =
                CoinListUiState(
                    coins = (1..10).map { previewModel },
                ),
            events = emptyFlow(),
        )
    }
}
