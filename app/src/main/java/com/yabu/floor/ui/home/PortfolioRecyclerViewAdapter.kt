package com.yabu.floor.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.robinhood.ticker.TickerUtils
import com.yabu.floor.data.model.portfolio.PortfolioItem
import com.yabu.floor.databinding.PortfolioItemBinding
import com.yabu.floor.utils.OnRecyclerViewItemClick

/**
 * Adapter for a Portfolio.
 * @param items
 */
class PortfolioRecyclerViewAdapter(private val items: List<PortfolioItem>,
                                   private val onClickListener: OnRecyclerViewItemClick)
    : RecyclerView.Adapter<PortfolioRecyclerViewAdapter.PortfolioItemViewHolder>() {
    private var viewState: PortfolioVewState = PortfolioVewState.DEFAULT

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioItemViewHolder {
        val binding =
            PortfolioItemBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false)
        binding.portfolioListItemTickerPrice.setCharacterLists(TickerUtils.provideNumberList())
        binding.portfolioListItemTickerPercent.setCharacterLists(TickerUtils.provideNumberList())
        return PortfolioItemViewHolder(binding, onClickListener)
    }

    override fun onBindViewHolder(holder: PortfolioItemViewHolder, position: Int) {
        holder.bind(items[position],
            viewState)
    }

    fun updateViewState(state: PortfolioVewState) {
        viewState = state
    }

    fun moveItem(from: Int, to: Int) {

    }

    override fun getItemCount() = items.size

    class PortfolioItemViewHolder(private val binding: PortfolioItemBinding,
                                  private val onClickListener: OnRecyclerViewItemClick)
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            onClickListener.onItemClick(v, adapterPosition)
        }

        fun bind(item: PortfolioItem, viewType: PortfolioVewState) {
            binding.portfolioItem = item
            binding.isCompact = viewType == PortfolioVewState.COMPACT
            binding.executePendingBindings()
        }
    }
}