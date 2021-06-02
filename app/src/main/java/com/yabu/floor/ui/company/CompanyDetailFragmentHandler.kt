package com.yabu.floor.ui.company

import android.view.HapticFeedbackConstants
import android.view.View
import androidx.navigation.findNavController
import com.yabu.floor.data.model.iex.Company
import com.yabu.floor.data.model.portfolio.Portfolio
import com.yabu.floor.data.model.portfolio.Ticker
import com.yabu.floor.data.repositories.PortfolioRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CompanyDetailFragmentHandler @Inject constructor(
    private val portfolioRepository: PortfolioRepository) {
    fun onBackClick(v: View) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)

        v.findNavController().navigateUp()
    }

    fun onWatchClick(v: View, portfolio: Portfolio, company: Company) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)

        val portfolioItem = portfolio.items.find { it.ticker.id == company.symbol }
        if (portfolioItem !== null) {
            portfolio.remove(company.symbol)
        } else {
            portfolio.add(Ticker(company.symbol, company.companyName))
        }

        CoroutineScope(Dispatchers.Default).launch {
            portfolioRepository.update(portfolio)
        }
    }
}