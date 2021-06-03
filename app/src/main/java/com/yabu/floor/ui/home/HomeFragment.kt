package com.yabu.floor.ui.home

import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.yabu.floor.R
import com.yabu.floor.data.Status
import com.yabu.floor.data.model.portfolio.Portfolio
import com.yabu.floor.data.model.portfolio.PortfolioItem
import com.yabu.floor.databinding.FragmentHomeBinding
import com.yabu.floor.ui.company.CompanyDetailFragment
import com.yabu.floor.ui.settings.features.Feature
import com.yabu.floor.ui.settings.features.FeatureToggleHelper
import com.yabu.floor.utils.OnRecyclerViewItemClick
import com.yabu.floor.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * Displays the Floored home screen.
 * The main ticker portfolio list is shown here.
 */
@AndroidEntryPoint
class HomeFragment : Fragment(),
    MaterialButtonToggleGroup.OnButtonCheckedListener,
    OnRecyclerViewItemClick {
    private val logtag = this::class.simpleName

    @Inject
    lateinit var handler: HomeFragmentHandler

    @Inject
    lateinit var featureToggle: FeatureToggleHelper

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels()

    private var resourceStatus = ObservableField(Status.LOADING)

    private val portfolioItems: MutableList<PortfolioItem> = mutableListOf()

    private var portfolio: Portfolio? = null

    private val itemTouchHelper by lazy {
        // 1. Note that I am specifying all 4 directions.
        //    Specifying START and END also allows
        //    more organic dragging than just specifying UP and DOWN.
        val simpleItemTouchCallback =
            object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or
                    ItemTouchHelper.DOWN or
                    ItemTouchHelper.START or
                    ItemTouchHelper.END, 0) {
                // 1. This callback is called when a ViewHolder is selected.
                //    We highlight the ViewHolder here.
                override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?,
                                               actionState: Int) {
                    super.onSelectedChanged(viewHolder, actionState)

                    if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                        viewHolder?.itemView?.alpha = 0.5f
                    }
                }

                // 2. This callback is called when the ViewHolder is
                //    unselected (dropped). We unhighlight the ViewHolder here.
                override fun clearView(recyclerView: RecyclerView,
                                       viewHolder: RecyclerView.ViewHolder) {
                    super.clearView(recyclerView, viewHolder)
                    viewHolder.itemView.alpha = 1.0f
                }

                override fun onMove(recyclerView: RecyclerView,
                                    viewHolder: RecyclerView.ViewHolder,
                                    target: RecyclerView.ViewHolder): Boolean {
                    lifecycleScope.launch {
                        val adapter = recyclerView.adapter as PortfolioRecyclerViewAdapter
                        val from = viewHolder.absoluteAdapterPosition
                        val to = target.absoluteAdapterPosition
                        // 2. Update the backing model. Custom implementation in
                        //    MainRecyclerViewAdapter. You need to implement
                        //    reordering of the backing model inside the method.
                        viewModel.moveItem(from, to, portfolio)

                        // 3. Tell adapter to render the model update.
                        adapter.notifyItemMoved(from, to)
                    }

                    return true
                }
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder,
                                      direction: Int) {
                    // 4. Code block for horizontal swipe.
                    //    ItemTouchHelper handles horizontal swipe as well, but
                    //    it is not relevant with reordering. Ignoring here.
                }
            }
        ItemTouchHelper(simpleItemTouchCallback)
    }

    // Search Bar focus listener to navigate to SearchFragment.
    private val onSearchBarClick = View.OnClickListener { v ->
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        findNavController().navigate(R.id.action_global_search)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_home,
            container,
            false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@HomeFragment.viewModel
            handler = this@HomeFragment.handler
            status = resourceStatus
            homeSearchBar.setSearchBarClick(onSearchBarClick)
        }

        firebaseAnalytics = Firebase.analytics

        if (featureToggle.isActivated(Feature.PORTFOLIO_LIST)) {
            binding.homePortfolio.visibility = View.VISIBLE
            binding.homePortfolio.adapter = PortfolioRecyclerViewAdapter(portfolioItems,
                this@HomeFragment)
            itemTouchHelper.attachToRecyclerView(binding.homePortfolio)

            val currentPortfolio = Portfolio.getCurrentPortfolio(context)
            getPortfolioItems(currentPortfolio)
        }

        if (featureToggle.isActivated(Feature.PORTFOLIO_VIEW_TOGGLE)) {
            binding.homePortfolioToggleGroup.visibility = View.VISIBLE
            binding.homePortfolioToggleGroup.addOnButtonCheckedListener(this@HomeFragment)
        }

        return binding.root
    }

    private fun getPortfolioItems(name: String) {
        lifecycleScope.launch {
            portfolio = viewModel.getPortfolio(name)

            if (portfolio == null) {
                resourceStatus.set(Status.ERROR)
                return@launch
            }

            resourceStatus.set(Status.SUCCESS)
            portfolioItems.clear()
            portfolioItems.addAll(portfolio?.items ?: listOf())

            // Once we have the portfolio tickers, we can send out calls to
            // fetch the quotes and historical data for the chart.
            getQuotes()
            getDatasets()
        }
    }

    private fun getQuotes() {
        lifecycleScope.launch {

            portfolioItems.map { it.ticker.id }.apply {
                val res = viewModel.getBatchQuotes(this)

                when (res.status) {
                    Status.SUCCESS -> {
                        portfolioItems.forEach { item ->
                            item.quote = res.data?.find {
                                it.symbol == item.ticker.id
                            }
                        }
                        binding.homePortfolio.adapter
                            ?.notifyItemRangeChanged(0, portfolioItems.size)
                    }
                    Status.ERROR ->
                        Log.d(logtag, "Error fetching batch quotes: ${res.message}")
                    Status.LOADING -> { }
                }
            }
        }
    }

    private fun getDatasets() {
        lifecycleScope.launch {
            Log.d(logtag, "Fetching Datasets.")

            portfolioItems.map { it.ticker.id }.apply {
                val res = viewModel.getPortfolioChartHistoricalDatasets(this)
                when (res.status) {
                    Status.SUCCESS -> {
                        Log.d(logtag, "Fetched Datasets.")
                        portfolioItems.forEach { item ->
                            val chartHistoricalData = res.data?.find {
                                it.symbol == item.ticker.id
                            }

                            item.intradayData = chartHistoricalData?.filterIntraday()
                            item.historicalData = chartHistoricalData?.filterNonIntraday()
                        }

                        binding.homePortfolio.adapter
                            ?.notifyItemRangeChanged(0, portfolioItems.size)
                    }
                    Status.ERROR ->
                        Log.d(logtag, "Error fetching batch datasets: ${res.message}")
                    Status.LOADING -> {
                    }
                }
            }
        }
    }

    override fun onButtonChecked(group: MaterialButtonToggleGroup?,
                                 checkedId: Int,
                                 isChecked: Boolean) {
        try {
            val viewState = when (checkedId) {
                R.id.home_portfolio_toggle_default -> PortfolioVewState.DEFAULT
                R.id.home_portfolio_toggle_compact -> PortfolioVewState.COMPACT
                else -> PortfolioVewState.DEFAULT
            }

            val adapter = binding.homePortfolio.adapter as? PortfolioRecyclerViewAdapter

            adapter?.updateViewState(viewState)

            binding.homePortfolio.adapter
                ?.notifyItemRangeChanged(0, portfolioItems.size)

            // Store the view state in the shared preferences.
            Utils.getSharedPreferences(requireContext()).edit()
                .putInt(Portfolio.PORTFOLIO_VIEW_STATE_KEY, viewState.ordinal)
                .apply()
        } catch (e: IllegalArgumentException) {
            Log.e(logtag, e.message.toString())
        }
    }

    override fun onItemClick(v: View, position: Int) {
        v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)

        if (position >= portfolioItems.size) {
            Log.e(logtag, "$position search item out of bounds.")
            return
        }

        val ticker = portfolioItems[position].ticker

        // Log the event with Firebase.
        val eventBundle = bundleOf(FirebaseAnalytics.Param.ITEM_ID to ticker.id,
            FirebaseAnalytics.Param.ITEM_NAME to ticker.name,
            FirebaseAnalytics.Param.CONTENT_TYPE to "ticker")

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, eventBundle)

        val bundle = bundleOf(
            CompanyDetailFragment.ARG_SYMBOL to portfolioItems[position].ticker.id
        )

        findNavController().navigate(R.id.action_global_company_detail, bundle)
    }
}