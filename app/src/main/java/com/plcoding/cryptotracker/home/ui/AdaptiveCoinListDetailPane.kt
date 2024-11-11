package com.plcoding.cryptotracker.home.ui

import android.widget.Toast
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.plcoding.cryptotracker.R
import com.plcoding.cryptotracker.core.ui.ObserveAsEvents
import com.plcoding.cryptotracker.detail.presentation.model.CoinDetailAction
import com.plcoding.cryptotracker.detail.ui.component.CoinDetailScreen
import com.plcoding.cryptotracker.detail.ui.model.CoinDetailUiState
import com.plcoding.cryptotracker.overview.presentation.model.CoinListAction
import com.plcoding.cryptotracker.overview.presentation.model.CoinListEvent
import com.plcoding.cryptotracker.overview.ui.component.CoinListScreen
import com.plcoding.cryptotracker.overview.ui.model.CoinListUiState
import kotlinx.coroutines.flow.SharedFlow

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AdaptiveCoinListDetailPane(
    modifier: Modifier = Modifier,
    coinListUiState: CoinListUiState,
    coinDetailUiState: CoinDetailUiState,
    events: SharedFlow<CoinListEvent>,
    onListAction: (CoinListAction) -> Unit,
    onDetailAction: (CoinDetailAction) -> Unit,
) {
    val context = LocalContext.current
    ObserveAsEvents(events = events) {
        when (it) {
            is CoinListEvent.Error -> {
                Toast
                    .makeText(
                        context,
                        context.getString(R.string.error),
                        Toast.LENGTH_SHORT,
                    ).show()
            }
        }
    }

    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    val listState = rememberLazyListState()
    NavigableListDetailPaneScaffold(
        modifier = modifier,
        navigator = navigator,
        listPane = {
            AnimatedPane {
                CoinListScreen(
                    coinListUiState = coinListUiState,
                    listState = listState,
                    events = events,
                    onAction = {
                        onListAction(it)
                        if (it is CoinListAction.OnCoinClick) {
                            navigator.navigateTo(pane = ListDetailPaneScaffoldRole.Detail)
                        }
                    },
                )
            }
        },
        detailPane = {
            AnimatedPane {
                CoinDetailScreen(
                    state = coinDetailUiState,
                    onAction = onDetailAction,
                )
            }
        },
    )
}
