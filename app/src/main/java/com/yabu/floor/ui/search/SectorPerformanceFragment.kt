package com.yabu.floor.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.yabu.floor.data.model.iex.SectorPerformance
import com.yabu.floor.databinding.FragmentSectorPerformanceBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SectorPerformanceFragment : Fragment() {
    private lateinit var binding: FragmentSectorPerformanceBinding

    private val viewModel: SearchViewModel by viewModels()

    private val sectors: MutableList<SectorPerformance> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentSectorPerformanceBinding.inflate(inflater, container, false)

        binding.apply {
            sectorPerformanceList.adapter = SectorPerformanceAdapter(sectors)
        }
        observeSectorPerformance()

        return binding.root
    }

    private fun observeSectorPerformance() {
        lifecycleScope.launch {
            val resource = viewModel.getSectorPerformance()

            sectors.clear()
            sectors.addAll(resource.data ?: listOf())

            binding.sectorPerformanceList.adapter
                ?.notifyItemRangeChanged(0, sectors.size)

        }
    }
}