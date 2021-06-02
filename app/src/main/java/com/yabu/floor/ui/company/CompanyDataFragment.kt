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
import com.yabu.floor.data.model.iex.Company
import com.yabu.floor.databinding.FragmentCompanyDataBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A modular [Fragment] displaying Company data such as description and location.
 */
@AndroidEntryPoint
class CompanyDataFragment : Fragment() {
    private val logtag = this::class.simpleName

    @Inject
    lateinit var handler: CompanyDataFragmentHandler

    // Injected through the fragment arguments onCreate.
    private var symbol: String? = null

    private val company: ObservableField<Company> = ObservableField()

    private lateinit var binding: FragmentCompanyDataBinding

    private val viewModel: CompanyDetailViewModel by viewModels()

    private var resourceStatus: ObservableField<Status> = ObservableField()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_company_data,
            container,
            false
        )
        binding.apply {
            handler = this@CompanyDataFragment.handler
            status = resourceStatus
            company = this@CompanyDataFragment.company
            companyDataParent.setOnLongClickListener { v ->
                this@CompanyDataFragment.handler.onLongPress(v)
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

        getCompany(symbol)
    }

    private fun getCompany(symbol: String) {
        lifecycleScope.launch {
            val resource = viewModel.getCompany(symbol)
            resourceStatus.set(resource.status)
            when (resource.status) {
                Status.SUCCESS -> company.set(resource.data)
                Status.ERROR ->
                    Log.d(logtag, "Error fetching quote: ${resource.message}")
                Status.LOADING -> {}
            }
        }
    }
}