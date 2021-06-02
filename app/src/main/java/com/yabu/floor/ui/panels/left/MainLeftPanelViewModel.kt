package com.yabu.floor.ui.panels.left

import androidx.lifecycle.ViewModel
import com.yabu.floor.data.model.portfolio.Portfolio
import com.yabu.floor.data.repositories.PortfolioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainLeftPanelViewModel @Inject constructor(
    private val repository: PortfolioRepository) : ViewModel() {

    suspend fun subscribeAllUserPortfolios() = repository.subscribeAllUserPortfolios()

    suspend fun addPortfolio(name: String) {
        val portfolio = Portfolio(
                id = UUID.randomUUID().toString(),
                name = name,
                visibleToUser = true
        )

        repository.insert(portfolio)
    }
}