package com.plcoding.cryptotracker.di

import com.plcoding.cryptotracker.core.data.network.HttpClientFactory
import com.plcoding.cryptotracker.detail.data.web.mapper.CoinPriceWebToDomainMapper
import com.plcoding.cryptotracker.detail.domain.usecase.GetPriceHistoryUseCase
import com.plcoding.cryptotracker.detail.presentation.mapper.CoinPriceHistoryDomainToPresentationMapper
import com.plcoding.cryptotracker.detail.ui.mapper.CoinDetailPresentationStateToUiMapper
import com.plcoding.cryptotracker.overview.data.CoinRemoteRepository
import com.plcoding.cryptotracker.overview.data.RemoteCoinDataSource
import com.plcoding.cryptotracker.overview.data.web.CoinCapWebDataSource
import com.plcoding.cryptotracker.overview.data.web.mapper.CoinWebToDomainMapper
import com.plcoding.cryptotracker.overview.domain.CoinRepository
import com.plcoding.cryptotracker.overview.domain.usecase.GetCoinsUseCase
import com.plcoding.cryptotracker.overview.presentation.CoinListViewModel
import com.plcoding.cryptotracker.overview.presentation.mapper.CoinDomainToPresentationMapper
import com.plcoding.cryptotracker.overview.ui.mapper.CoinListStatePresentationToUiMapper
import com.plcoding.cryptotracker.overview.ui.mapper.CoinPresentationToUiMapper
import io.ktor.client.engine.cio.CIO
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule =
    module {
        single { HttpClientFactory.create(CIO.create()) }
        singleOf(::CoinWebToDomainMapper)
        singleOf(::CoinPriceWebToDomainMapper)
        singleOf(::CoinDomainToPresentationMapper)
        singleOf(::CoinPriceHistoryDomainToPresentationMapper)
        singleOf(::CoinListStatePresentationToUiMapper)
        singleOf(::CoinDetailPresentationStateToUiMapper)
        singleOf(::CoinPresentationToUiMapper)

        singleOf(::GetCoinsUseCase)
        singleOf(::GetPriceHistoryUseCase)

        singleOf(::CoinCapWebDataSource).bind<RemoteCoinDataSource>()
        singleOf(::CoinRemoteRepository).bind<CoinRepository>()

        viewModelOf(::CoinListViewModel)
    }
