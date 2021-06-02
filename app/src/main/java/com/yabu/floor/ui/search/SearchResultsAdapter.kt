package com.yabu.floor.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yabu.floor.data.model.iex.SearchItem
import com.yabu.floor.databinding.SearchListItemBinding
import com.yabu.floor.utils.OnRecyclerViewItemClick

class SearchResultsAdapter(private val items: MutableList<SearchItem>,
                           private val onClickListener: OnRecyclerViewItemClick)
    : RecyclerView.Adapter<SearchResultsAdapter.SearchItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        val binding =
            SearchListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        return SearchItemViewHolder(binding, onClickListener)
    }

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class SearchItemViewHolder(private val binding: SearchListItemBinding,
                               private val onClickListener: OnRecyclerViewItemClick)
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            onClickListener.onItemClick(v, adapterPosition)
        }

        fun bind(item: SearchItem) {
            binding.item = item
            binding.executePendingBindings()
        }
    }
}