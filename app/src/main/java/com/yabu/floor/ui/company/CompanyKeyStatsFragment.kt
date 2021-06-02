package com.yabu.floor.ui.company

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.yabu.floor.R
import com.yabu.floor.data.Status
import com.yabu.floor.data.model.iex.KeyStats
import com.yabu.floor.databinding.FragmentCompanyKeyStatsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CompanyKeyStatsFragment : Fragment() {
    private val logtag = this::class.simpleName

    @Inject
    lateinit var handler: CompanyKeyStatsFragmentHandler

    // Injected through the fragment arguments onCreate.
    private var symbol: String? = null

    private val keyStats: ObservableField<KeyStats> = ObservableField()

    private lateinit var binding: FragmentCompanyKeyStatsBinding

    private val viewModel: CompanyDetailViewModel by viewModels()

    var resourceStatus: ObservableField<Status> = ObservableField()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_company_key_stats,
            container,
            false
        )
        binding.apply {
            status = resourceStatus
            keyStats = this@CompanyKeyStatsFragment.keyStats
            handler = this@CompanyKeyStatsFragment.handler
            companyKeyStatsParent.setOnLongClickListener { v ->
                this@CompanyKeyStatsFragment.handler.onLongPress(v)
            }
        }

        return binding.root
    }

    /**
     * Set the symbol this fragment looks up historical data for.
     * @param symbol
     */
    fun setSymbol(symbol: String?) {
        if (symbol == null) {
            return
        }
        this.symbol = symbol

        getKeyStats(symbol)
    }

    private fun getKeyStats(symbol: String) {
        lifecycleScope.launch {
            val resource = viewModel.getKeyStats(symbol)
            resourceStatus.set(resource.status)
            when (resource.status) {
                Status.SUCCESS -> keyStats.set(resource.data)
                Status.ERROR ->
                    Log.d(logtag, "Error fetching quote: ${resource.message}")
                Status.LOADING -> {}
            }
        }
    }
}