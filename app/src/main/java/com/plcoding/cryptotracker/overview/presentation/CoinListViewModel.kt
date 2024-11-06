package com.plcoding.cryptotracker.overview.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.cryptotracker.core.domain.onError
import com.plcoding.cryptotracker.core.domain.onSuccess
import com.plcoding.cryptotracker.detail.domain.model.CoinPrice
import com.plcoding.cryptotracker.detail.domain.usecase.GetPriceHistoryUseCase
import com.plcoding.cryptotracker.detail.presentation.mapper.CoinPriceHistoryDomainToPresentationMapper
import com.plcoding.cryptotracker.detail.presentation.model.CoinDetailAction
import com.plcoding.cryptotracker.detail.presentation.model.CoinDetailPresentationState
import com.plcoding.cryptotracker.overview.domain.model.Coin
import com.plcoding.cryptotracker.overview.domain.model.CoinId
import com.plcoding.cryptotracker.overview.domain.usecase.GetCoinsUseCase
import com.plcoding.cryptotracker.overview.presentation.mapper.CoinDomainToPresentationMapper
import com.plcoding.cryptotracker.overview.presentation.model.CoinListAction
import com.plcoding.cryptotracker.overview.presentation.model.CoinListEvent
import com.plcoding.cryptotracker.overview.presentation.model.CoinListPresentationState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class CoinListViewModel(
    private val getCoins: GetCoinsUseCase,
    private val getPriceHistory: GetPriceHistoryUseCase,
    private val coinDomainToPresentationMapper: CoinDomainToPresentationMapper,
    private val coinPriceHistoryDomainToPresentationMapper: CoinPriceHistoryDomainToPresentationMapper,
) : ViewModel() {
    private val _state = MutableStateFlow(CoinListPresentationState())
    val state =
        _state
            .onStart {
                _state.update { it.copy(isLoading = true) }
                loadCoins()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TimeUnit.SECONDS.toMillis(5)),
                initialValue = CoinListPresentationState(),
            )

    private val _detailState = MutableStateFlow(CoinDetailPresentationState())
    val detailState =
        _detailState.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TimeUnit.SECONDS.toMillis(5)),
            initialValue = CoinDetailPresentationState(),
        )

    // private val _events = Channel<CoinListEvent>()
    // val events = _events.receiveAsFlow()
    private val _events = MutableSharedFlow<CoinListEvent>()
    val events = _events.asSharedFlow()

    fun onAction(action: CoinListAction) {
        when (action) {
            CoinListAction.OnRefresh -> {
                _state.update { it.copy(isRefreshing = true) }
                loadCoins()
            }

            is CoinListAction.OnCoinClick -> {
                selectCoin(action.coinId)
            }
        }
    }

    fun onAction(action: CoinDetailAction) {
        when (action) {
            CoinDetailAction.Back -> {
                _state.update { it.copy(selectedCoin = null) }
            }
        }
    }

    private fun loadCoins() {
        viewModelScope.launch {
            getCoins()
                .onSuccess { list: List<Coin> ->
                    val coins =
                        list.map { coin ->
                            coinDomainToPresentationMapper.toPresentation(coin)
                        }
                    _state.update {
                        it.copy(
                            coins = coins,
                            isLoading = false,
                            isRefreshing = false,
                        )
                    }
                }.onError { error ->
                    _events.emit(CoinListEvent.Error(error))
                    _state.update { it.copy(isLoading = false, isRefreshing = false) }
                    Log.e("CoinListViewModel", "Error loading coins $error")
                }
        }
    }

    private fun selectCoin(coinId: CoinId) {
        val coin = _state.value.coins.find { it.id == coinId }
        if (coin == null) return

        _state.update { it.copy(selectedCoin = coin) }
        _detailState.update { it.copy(coinDetail = coin, isLoading = true) }

        viewModelScope.launch {
            getPriceHistory(coinId)
                .onSuccess { list: List<CoinPrice> ->
                    val mapped = list.map {
                        coinPriceHistoryDomainToPresentationMapper.toPresentation(it)
                    }.takeLast(6)
                    _detailState.update {
                        it.copy(isLoading = false, coinHistory = mapped)
                    }
                }.onError { error ->
                    _events.emit(CoinListEvent.Error(error))
                    _detailState.update { it.copy(isLoading = false) }
                    Log.e("CoinListViewModel", "Error loading price history $error")
                }
        }
    }
}
