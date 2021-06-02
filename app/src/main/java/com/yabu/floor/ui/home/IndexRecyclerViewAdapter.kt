package com.yabu.floor.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.robinhood.ticker.TickerUtils
import com.yabu.floor.R
import com.yabu.floor.data.model.portfolio.PortfolioItem
import com.yabu.floor.databinding.IndexListItemBinding
import com.yabu.floor.utils.OnRecyclerViewItemClick

class IndexRecyclerViewAdapter(private val items: List<PortfolioItem>,
                               private val onClickListener: OnRecyclerViewItemClick)
    : RecyclerView.Adapter<IndexRecyclerViewAdapter.IndexItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndexItemViewHolder {
        val binding =
            IndexListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        binding.indexListItemTickerPercent.setCharacterLists(TickerUtils.provideNumberList())
        return IndexItemViewHolder(binding, onClickListener)
    }

    override fun onBindViewHolder(holder: IndexItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class IndexItemViewHolder(private val binding: IndexListItemBinding,
                                  private val onClickListener: OnRecyclerViewItemClick
    )
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private var screenWidth = 0f
        private var padding = 0f

        init {
            // Grab the screen width and height
            val metrics = binding.root.context.resources.displayMetrics
            padding = binding.root.context.resources.getDimension(R.dimen.paddingSmall)
            screenWidth = metrics.widthPixels.toFloat()
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            onClickListener.onItemClick(v, adapterPosition)
        }

        fun bind(item: PortfolioItem) {
            val lp = binding.indexListItemContainer.layoutParams
            lp.width = ((screenWidth - (padding*2)) / 3f).toInt()
            binding.indexListItemContainer.layoutParams = lp

            binding.portfolioItem = item
            binding.executePendingBindings()
        }
    }
}