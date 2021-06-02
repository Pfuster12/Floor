package com.yabu.floor.ui.panels.left

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yabu.floor.data.model.portfolio.Portfolio
import com.yabu.floor.databinding.PortfoliosListItemBinding
import com.yabu.floor.utils.OnRecyclerViewItemClick


/**
 * Adapter for a Portfolio.
 * @param items
 */
class PortfoliosListAdapter(private val items: List<Portfolio>,
                                   private val onClickListener: OnRecyclerViewItemClick
)
    : RecyclerView.Adapter<PortfoliosListAdapter.PortfolioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioViewHolder {
        val binding =
            PortfoliosListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        return PortfolioViewHolder(binding, onClickListener)
    }

    override fun onBindViewHolder(holder: PortfolioViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class PortfolioViewHolder(private val binding: PortfoliosListItemBinding,
                                  private val onClickListener: OnRecyclerViewItemClick
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            onClickListener.onItemClick(v, adapterPosition)
        }

        fun bind(item: Portfolio) {
            binding.portfolio = item
            binding.executePendingBindings()
        }
    }
}