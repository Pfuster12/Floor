package com.yabu.floor.ui.panels.left

import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.transition.TransitionManager
import com.yabu.floor.R
import com.yabu.floor.data.model.portfolio.Portfolio
import com.yabu.floor.databinding.FragmentMainLeftPanelBinding
import com.yabu.floor.utils.OnRecyclerViewItemClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainLeftPanelFragment : Fragment(),
    OnRecyclerViewItemClick {
    private lateinit var binding: FragmentMainLeftPanelBinding

    private val portfolios: MutableList<Portfolio> = mutableListOf()

    private val viewModel: MainLeftPanelViewModel by viewModels()

    private fun subscribeAllUserPortfolios() {
        lifecycleScope.launch {
            viewModel.subscribeAllUserPortfolios().observe(viewLifecycleOwner) { resource ->
                portfolios.clear()

                portfolios.addAll(resource)

                binding.mainLeftPanelPortfoliosList
                    .adapter
                    ?.notifyItemRangeChanged(0, portfolios.size)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_main_left_panel,
                container,
                false)

        binding.mainLeftPanelPortfoliosList.adapter =
                PortfoliosListAdapter(portfolios, this)

        binding.apply {
            mainLeftPanelAddPortfolioButton.setOnClickListener(createPortfolioButtonOnClick)
            mainLeftPanelAddPortfolioInput.setOnEditorActionListener(addPortfolioAction)
        }

        subscribeAllUserPortfolios()

        return binding.root
    }

    // Display the Add Portfolio Input Layout.
    private val createPortfolioButtonOnClick = View.OnClickListener { v ->
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)

        TransitionManager.beginDelayedTransition(binding.mainLeftPanelLayout)

        if (binding.mainLeftPanelAddPortfolioInputLayout.visibility == View.GONE) {
            binding.mainLeftPanelAddPortfolioInputLayout.visibility = View.VISIBLE
        } else {
            binding.mainLeftPanelAddPortfolioInputLayout.visibility = View.GONE
        }
    }

    //  Add Portfolio.
    private val addPortfolioAction = TextView.OnEditorActionListener { v, actionId, _ ->
        return@OnEditorActionListener if (actionId == EditorInfo.IME_ACTION_DONE) {
            val name = binding.mainLeftPanelAddPortfolioInput.text.toString()

            if (name.isNotEmpty()) {
                lifecycleScope.launch {
                    viewModel.addPortfolio(name)

                    // Clear and Hide input layout.
                    binding.mainLeftPanelAddPortfolioInput.setText("")
                    binding.mainLeftPanelAddPortfolioInputLayout.visibility = View.GONE
                }
            }

            false
        } else {
            true
        }
    }

    override fun onItemClick(v: View, position: Int) {
        val item = portfolios[position]

        Portfolio.saveCurrentPortfolio(context, item.id)
    }
}