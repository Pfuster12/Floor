package com.yabu.floor.ui.markets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.yabu.floor.R
import com.yabu.floor.databinding.FragmentMarketsBinding

/**
 * Displays Markets tab.
 */
class MarketsFragment : Fragment() {
    private lateinit var binding: FragmentMarketsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_markets,
            container,
            false)
        // Inflate the layout for this fragment
        return binding.root
    }
}