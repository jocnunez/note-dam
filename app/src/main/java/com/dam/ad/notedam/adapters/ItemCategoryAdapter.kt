package com.dam.ad.notedam.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dam.ad.notedam.R
import com.dam.ad.notedam.databinding.ItemCategoryLayoutBinding
import com.dam.ad.notedam.models.Category

class ItemCategoryAdapter(private var listItem: MutableList<Category>, private var listener: OnElementClickListener<Category>) : RecyclerView.Adapter<ItemCategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_layout, parent, false)
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

        private val binding = ItemCategoryLayoutBinding.bind(view)

        fun bind(item: Category) {
            binding.title.text = item.title
            binding.text.text = item.description
            binding.priority.text = item.priority.toString()
            binding.upButton.setOnClickListener {
                item.priority += 1u
                binding.priority.text = item.priority.toString()
                listItem.sortBy { it.priority }
                notifyDataSetChanged()
            }
            binding.downButton.setOnClickListener {
                if (item.priority > 0u) {
                    item.priority -= 1u
                    binding.priority.text = item.priority.toString()
                    listItem.sortBy { it.priority }
                    notifyDataSetChanged()
                }
            }
        }

        fun setListener(category: Category) {

            binding.root.setOnClickListener {
                listener.onClick(category)
            }
            binding.root.setOnLongClickListener {
                listener.onLongClick(category)
                true
            }
        }

    }

    fun setCategories(categories: MutableList<Category>) {
        listItem = categories
        notifyDataSetChanged()
    }

    fun add(category: Category) {
        listItem.add(category)
        notifyItemInserted(listItem.size - 1)
    }

    fun delete(category: Category) {
        val index = listItem.indexOf(category)
        if (index != -1) {
            listItem.removeAt(index)
            notifyItemRemoved(index)
        }
    }

}