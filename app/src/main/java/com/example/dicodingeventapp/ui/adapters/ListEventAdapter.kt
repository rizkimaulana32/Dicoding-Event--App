package com.example.dicodingeventapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingeventapp.data.remote.response.ListEventsItem
import com.example.dicodingeventapp.databinding.ItemRowListEventVerticalBinding

class ListEventAdapter(private val onItemClick: (ListEventsItem) -> Unit) : ListAdapter<ListEventsItem, ListEventAdapter.ListViewHolder>(
    DIFF_CALLBACK
) {
    class ListViewHolder(private val binding: ItemRowListEventVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listEventsItem: ListEventsItem, onItemClick: (ListEventsItem) -> Unit) {
            binding.tvNameVr.text = listEventsItem.name
            binding.tvSummaryVr.text = listEventsItem.summary
            Glide.with(binding.ivImgVr.context)
                .load(listEventsItem.imageLogo)
                .into(binding.ivImgVr)

            itemView.setOnClickListener {
                onItemClick(listEventsItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemRowListEventVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val listEventsItem = getItem(position)
        holder.bind(listEventsItem, onItemClick)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}