package com.yabu.floor.ui.search

import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yabu.floor.R
import com.yabu.floor.data.model.iex.SearchItem
import com.yabu.floor.data.model.search.SearchHistoryItem
import com.yabu.floor.databinding.FragmentSearchBinding
import com.yabu.floor.ui.company.CompanyDetailFragment
import com.yabu.floor.ui.components.searchbar.SearchBarLayout
import com.yabu.floor.utils.OnRecyclerViewItemClick
import com.yabu.floor.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(),
    OnRecyclerViewItemClick,
    SearchBarLayout.OnSearchListener {
    private val logtag = this::class.simpleName

    companion object {
        const val ARG_QUERY = "floored.ARG_SYMBOL"
    }
    private var initialQuery: String? = null

    private val viewModel: SearchViewModel by viewModels()

    private lateinit var binding: FragmentSearchBinding

    private val searchHistory: MutableList<SearchHistoryItem> = mutableListOf()

    private val searchItems: MutableList<SearchItem> = mutableListOf()

    private val onSearchFocus = View.OnFocusChangeListener { _, hasFocus ->
        if (hasFocus) {
            Utils.showKeyboard(binding.searchSearchBar)
        }
    }

    private val onSearchReturnClick = View.OnClickListener { v  ->
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        activity?.onBackPressed()
    }

    private val onSearchHistoryClick = object : OnRecyclerViewItemClick {
        override fun onItemClick(v: View, position: Int) {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            if (position >= searchHistory.size) {
                Log.e(logtag, "$position search history item out of bounds.")
                return
            }

            val item = searchHistory[position]

            val bundle = bundleOf(
                CompanyDetailFragment.ARG_SYMBOL to item.ticker
            )

            findNavController().navigate(R.id.action_global_company_detail, bundle)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            initialQuery = it.getString(ARG_QUERY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.apply {
            searchItems = this@SearchFragment.searchItems
            searchHistory = this@SearchFragment.searchHistory
            searchSearchBar.setReturnIcon()
            searchSearchBar.setSearchBarReturnClick(onSearchReturnClick)
            searchSearchBar.setSearchListener(this@SearchFragment)
            searchSearchBar.setSearchBarQuery(initialQuery)
            searchSearchBar.setSearchBarFocus(onSearchFocus)
            searchSearchBar.setEditTextFocusable()
            searchResultsList.adapter = SearchResultsAdapter(
                this@SearchFragment.searchItems,
                this@SearchFragment)
            searchHistoryList.adapter = SearchHistoryAdapter(
                this@SearchFragment.searchHistory,
                onSearchHistoryClick)
        }

        Utils.showKeyboard(binding.searchSearchBar.getSearchBar())

        getSearchHistory()

        if (!initialQuery.isNullOrBlank())
            performSearch(initialQuery)

        return binding.root
    }

    private fun getSearchHistory() {
        lifecycleScope.launch {
            val results = viewModel.getSearcHistory()

            searchHistory.clear()
            searchHistory.addAll(results)

            binding.searchHistoryList.adapter
                ?.notifyDataSetChanged()

            binding.searchHistory = searchHistory
        }
    }

    override fun onSearch(query: String) {
        // Handle a blank query as a 'clear' action
        if (query.isBlank()) {
            val oldCount = searchItems.size
            searchItems.clear()

            binding.searchResultsList.adapter
                ?.notifyItemRangeRemoved(0, oldCount)

            binding.searchItems = searchItems
            return
        }

        performSearch(query)
    }

    private fun performSearch(query: String?) {
        lifecycleScope.launch {
            if (query.isNullOrEmpty())
                return@launch

            binding.searchNoResults.visibility = View.GONE

            val resource = viewModel.getSearch(query)

            searchItems.clear()
            searchItems.addAll(resource.data ?: listOf())

            binding.searchResultsList.adapter
                ?.notifyItemRangeChanged(0, searchItems.size)

            binding.searchItems = searchItems

            if (searchItems.size == 0)
                binding.searchNoResults.visibility = View.VISIBLE

            Utils.hideKeyboard(activity)
        }
    }

    override fun onItemClick(v: View, position: Int) {
        lifecycleScope.launch {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            if (position >= searchItems.size) {
                Log.e(logtag, "$position search item out of bounds.")
                return@launch
            }

            val item = searchItems[position]
            if (searchItems.size > 0) {
                viewModel.insertSearcHistory(
                    SearchHistoryItem(
                        ticker = item.symbol,
                        name = item.securityName
                    )
                )
            }

            val bundle = bundleOf(
                CompanyDetailFragment.ARG_SYMBOL to item.symbol
            )

            findNavController().navigate(R.id.action_global_company_detail, bundle)
        }
    }
}