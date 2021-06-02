package com.yabu.floor.ui.main

import androidx.lifecycle.ViewModel
import com.yabu.floor.data.model.portfolio.Portfolio
import com.yabu.floor.data.repositories.IEXCloudRepository
import com.yabu.floor.data.repositories.PortfolioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PortfolioRepository,
    private val iexRepository: IEXCloudRepository
) : ViewModel() {

    suspend fun insertPortfolio(portfolio: Portfolio) = repository.insert(portfolio)
}