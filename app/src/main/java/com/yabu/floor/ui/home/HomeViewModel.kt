package com.yabu.floor.ui.home

import androidx.lifecycle.ViewModel
import com.yabu.floor.data.model.portfolio.Portfolio
import com.yabu.floor.data.repositories.IEXCloudRepository
import com.yabu.floor.data.repositories.PortfolioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PortfolioRepository,
    private val iexRepository: IEXCloudRepository) : ViewModel() {

    suspend fun querySearch(keyword: String) = iexRepository.querySearch(keyword)

    suspend fun moveItem(from: Int, to: Int, portfolio: Portfolio?) {
        repository.moveItem(from, to, portfolio)
    }

    suspend fun getPortfolio(id: String) = repository.get(id)

    suspend fun getPortfolioItems(id: String) = repository.getItems(id)

    suspend fun getBatchQuotes(symbols: List<String>) = iexRepository.batchQuotes(symbols)

    suspend fun getPortfolioChartHistoricalDatasets(symbols: List<String>) =
        iexRepository.batchChartHistoricalDataSets(
            symbols = symbols)
}