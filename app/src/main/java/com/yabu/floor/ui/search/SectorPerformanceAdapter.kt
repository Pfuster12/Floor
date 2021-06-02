package com.yabu.floor.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.robinhood.ticker.TickerUtils
import com.yabu.floor.data.model.iex.SectorPerformance
import com.yabu.floor.databinding.SectorPerformanceItemBinding

class SectorPerformanceAdapter(private val items: List<SectorPerformance>)
    : RecyclerView.Adapter<SectorPerformanceAdapter.SectorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectorViewHolder {
        val binding =
            SectorPerformanceItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        binding.sectorPerformanceItemTicker.setCharacterLists(TickerUtils.provideNumberList())
        return SectorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SectorViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class SectorViewHolder(private val binding: SectorPerformanceItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
   
        fun bind(item: SectorPerformance) {
            binding.sector = item
            binding.executePendingBindings()
        }
    }
}