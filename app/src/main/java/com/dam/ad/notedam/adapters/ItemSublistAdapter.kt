package com.dam.ad.notedam.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dam.ad.notedam.R
import com.dam.ad.notedam.databinding.ItemCategoryLayoutBinding
import com.dam.ad.notedam.databinding.ItemSublistItemLayoutBinding
import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.SublistItem

class ItemSublistAdapter(private var listItem: MutableList<SublistItem>, private var listener: OnElementClickListener<SublistItem>) : RecyclerView.Adapter<ItemSublistAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sublist_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItem[position]
        holder.bind(item)
        holder.setListener(item)
    }

    inner class ViewHolder (view: View): RecyclerView.ViewHolder(view) {

        private val binding = ItemSublistItemLayoutBinding.bind(view)

        fun bind(item: SublistItem) {
            binding.text.text = item.subListValue
            binding.checkbox.isChecked = item.check
        }

        fun setListener(item: SublistItem) {

            binding.root.setOnClickListener {
                listener.onClick(item)
            }
            binding.root.setOnLongClickListener {
                listener.onLongClick(item)
                true
            }
        }

    }

    fun setItems(items: MutableList<SublistItem>) {
        listItem = items
        notifyDataSetChanged()
    }

    fun add(item: SublistItem) {
        listItem.add(item)
        notifyDataSetChanged()
    }

    fun delete(item: SublistItem) {
        val index = listItem.indexOf(item)
        if (index != -1) {
            listItem.removeAt(index)
            notifyItemRemoved(index)
        }
    }

}