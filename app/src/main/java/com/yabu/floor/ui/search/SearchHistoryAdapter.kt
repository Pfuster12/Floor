package com.yabu.floor.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yabu.floor.data.model.search.SearchHistoryItem
import com.yabu.floor.databinding.SearchHistoryListItemBinding
import com.yabu.floor.utils.OnRecyclerViewItemClick

class SearchHistoryAdapter(private val items: MutableList<SearchHistoryItem>,
                           private val onClickListener: OnRecyclerViewItemClick
) : RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        val binding =
            SearchHistoryListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        return SearchHistoryViewHolder(binding, onClickListener)
    }

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class SearchHistoryViewHolder(private val binding: SearchHistoryListItemBinding,
                               private val onClickListener: OnRecyclerViewItemClick
    )
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            onClickListener.onItemClick(v, adapterPosition)
        }

        fun bind(item: SearchHistoryItem) {
            binding.searchHistory = item
            binding.executePendingBindings()
        }
    }
}