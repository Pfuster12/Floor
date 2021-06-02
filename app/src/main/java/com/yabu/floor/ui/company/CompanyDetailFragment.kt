package com.yabu.floor.ui.company

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.robinhood.ticker.TickerUtils
import com.yabu.floor.R
import com.yabu.floor.data.Status
import com.yabu.floor.data.model.iex.Company
import com.yabu.floor.data.model.iex.Quote
import com.yabu.floor.data.model.portfolio.Portfolio
import com.yabu.floor.databinding.FragmentCompanyDetailBinding
import com.yabu.floor.ui.components.livechart.LiveChartFragment
import com.yabu.floor.ui.settings.features.Feature
import com.yabu.floor.ui.settings.features.FeatureToggleHelper
import com.yabu.livechart.model.DataPoint
import com.yabu.livechart.view.LiveChart
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CompanyDetailFragment : Fragment(),
    LiveChart.OnTouchCallback {
    private val logtag = this::class.simpleName

    companion object {
        const val ARG_SYMBOL = "floored.ARG_SYMBOL"
    }

    @Inject
    lateinit var handler: CompanyDetailFragmentHandler

    @Inject
    lateinit var featureToggle: FeatureToggleHelper

    private val viewModel: CompanyDetailViewModel by viewModels()

    private lateinit var binding: FragmentCompanyDetailBinding

    private var symbol: String? = null

    private var portfolio: ObservableField<Portfolio> = ObservableField()

    private var quote: ObservableField<Quote> = ObservableField()

    private var company: ObservableField<Company> = ObservableField()

    private var isInPortfolio: ObservableBoolean = ObservableBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            symbol = it.getString(ARG_SYMBOL)
        }

        // Return early in case of a null symbol argument.
        if (symbol == null) {
            this.findNavController().navigateUp()
            return
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_company_detail,
            container,
            false
        )
        binding.apply {
            companyDetailQuotePrice.setCharacterLists(TickerUtils.provideNumberList())
            companyDetailQuotePercentChange.setCharacterLists(TickerUtils.provideNumberList())
            handler = this@CompanyDetailFragment.handler
            company = this@CompanyDetailFragment.company
            portfolio = this@CompanyDetailFragment.portfolio
            isInPortfolio = this@CompanyDetailFragment.isInPortfolio
            quote = this@CompanyDetailFragment.quote
        }

        if (featureToggle.isActivated(Feature.COMPANY_KEY_STATS)) {
            binding.companyDetailKeyStatsFragment.visibility = View.VISIBLE

            (childFragmentManager.findFragmentById(
                R.id.company_detail_key_stats_fragment) as? CompanyKeyStatsFragment)?.apply {
                setSymbol(symbol)
            }
        }

        if (featureToggle.isActivated(Feature.COMPANY_DATA)) {
            binding.companyDetailCompanyDataFragment.visibility = View.VISIBLE

            (childFragmentManager.findFragmentById(
                R.id.company_detail_company_data_fragment) as? CompanyDataFragment)?.apply {
                setSymbol(symbol)
            }
        }

        if (featureToggle.isActivated(Feature.COMPANY_LIVE_CHART)) {
            binding.companyDetailLiveChartFragment.visibility = View.VISIBLE

            (childFragmentManager.findFragmentById(
                R.id.company_detail_live_chart_fragment) as? LiveChartFragment)?.apply {
                setSymbol(symbol)
            }
        }

        getCompany(symbol ?: "")
        getQuote(symbol ?: "")
        getPortfolio()

        return binding.root
    }

    private fun getQuote(symbol: String) {
        lifecycleScope.launch {
            val res = viewModel.getQuote(symbol)
            when (res.status) {
                Status.SUCCESS -> {
                    quote.set(res.data)
                    when (quote.get()?.isUSMarketOpen) {
                        true -> {
                            binding.companyDetailMarketOpenBadge.setBackgroundResource(R.drawable.pill_green_surface)
                            binding.companyDetailMarketOpenBadge.setTextAppearance(R.style.Badge_Green)
                        }
                        else -> {
                            binding.companyDetailMarketOpenBadge.setBackgroundResource(R.drawable.pill_primary_surface)
                            binding.companyDetailMarketOpenBadge.setTextAppearance(R.style.Badge)
                        }
                    }
                }
                Status.ERROR ->
                    Log.d(logtag, "Error fetching quote: ${res.message}")
                Status.LOADING -> {}
            }
        }
    }

    private fun getPortfolio() {
        lifecycleScope.launch {
            viewModel.subscribePortfolio(Portfolio.PORTFOLIO_ID).observe(viewLifecycleOwner) { resource ->
                portfolio.set(resource)
                val portfolioItem = resource?.items?.find { it.ticker.id == symbol }
                isInPortfolio.set(portfolioItem !== null)
            }
        }
    }

    private fun getCompany(symbol: String) {
        lifecycleScope.launch {
            val resource = viewModel.getCompany(symbol)
            when (resource.status) {
                Status.SUCCESS -> {
                    company.set(resource.data)
                }
                Status.LOADING -> {}
                Status.ERROR -> {}
            }
        }
    }

    override fun onTouchCallback(point: DataPoint) {
        binding.companyDetailQuotePrice.text = getString(R.string.two_decimal_template, point.y)
    }

    override fun onTouchFinished() {
        binding.companyDetailQuotePrice.text =
            getString(R.string.two_decimal_template,quote.get()?.latestPrice)

    }
}