package com.yabu.floor.ui.company

import androidx.lifecycle.ViewModel
import com.yabu.floor.data.repositories.IEXCloudRepository
import com.yabu.floor.data.repositories.PortfolioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CompanyDetailViewModel @Inject constructor(
    private val iexRepository: IEXCloudRepository,
    private val portfolioRepository: PortfolioRepository,
) : ViewModel() {

    suspend fun getPortfolio(id: String) = portfolioRepository.get(id)

    suspend fun subscribePortfolio(id: String) = portfolioRepository.subscribe(id)

    suspend fun getQuote(symbol: String) = iexRepository.getQuote(symbol)

    suspend fun getCompany(symbol: String) = iexRepository.getCompany(symbol)

    suspend fun getKeyStats(symbol: String) = iexRepository.getKeyStats(symbol)
}