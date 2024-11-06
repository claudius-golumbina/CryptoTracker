package com.plcoding.cryptotracker

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.plcoding.cryptotracker.core.ui.ObserveAsEvents
import com.plcoding.cryptotracker.detail.ui.component.CoinDetailScreen
import com.plcoding.cryptotracker.detail.ui.mapper.CoinDetailPresentationStateToUiMapper
import com.plcoding.cryptotracker.overview.presentation.CoinListViewModel
import com.plcoding.cryptotracker.overview.presentation.model.CoinListEvent
import com.plcoding.cryptotracker.overview.ui.component.CoinListScreen
import com.plcoding.cryptotracker.overview.ui.mapper.CoinListStatePresentationToUiMapper
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    private val mapper: CoinListStatePresentationToUiMapper by inject()
    private val detailMapper: CoinDetailPresentationStateToUiMapper by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptoTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel = koinViewModel<CoinListViewModel>()
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    val detailState by viewModel.detailState.collectAsStateWithLifecycle()
                    val listState = rememberLazyListState()

                    val isDetail = state.selectedCoin != null
                    AnimatedVisibility(
                        visible = isDetail,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        CoinDetailScreen(
                            state = detailMapper.toUi(detailState),
                            modifier = Modifier.padding(innerPadding),
                            onAction = viewModel::onAction,
                        )
                    }
                    AnimatedVisibility(
                        visible = !isDetail,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        CoinListScreen(
                            coinListUiState = mapper.toUi(state),
                            listState = listState,
                            events = viewModel.events,
                            modifier = Modifier.padding(innerPadding),
                            onAction = viewModel::onAction,
                        )
                    }

                    val context = LocalContext.current
                    ObserveAsEvents(events = viewModel.events) {
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
                }
            }
        }
    }
}
