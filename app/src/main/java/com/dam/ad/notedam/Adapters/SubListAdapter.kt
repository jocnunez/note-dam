package com.dam.ad.notedam.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dam.ad.notedam.Models.Categoria
import com.dam.ad.notedam.Models.nota.Nota
import com.dam.ad.notedam.Models.nota.SubList
import com.dam.ad.notedam.R
import com.dam.ad.notedam.databinding.ItemCategoriasBinding
import com.dam.ad.notedam.databinding.ItemSublistaBinding
import java.util.*

class SubListAdapter (
    private var listItem: MutableList<SubList>
) : RecyclerView.Adapter<SubListAdapter.ViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sublista, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItem[position]
        holder.bind(item)
        holder.setListener(item)
    }

    override fun getItemCount(): Int {
        return listItem.count()
    }

    fun removeElement(uuid: UUID) {
        listItem.removeIf { it.uuid == uuid }
        notifyDataSetChanged()
    }
    

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val binding = ItemSublistaBinding.bind(view)

        fun bind(subList: SubList) {
            binding.checkBoxElement.text = subList.texto
            binding.checkBoxElement.isChecked = subList.boolean
        }

        fun setListener(subList: SubList) {
            binding.checkBoxElement.setOnLongClickListener {
                removeElement(subList.uuid)
                true
            }
            binding.checkBoxElement.setOnCheckedChangeListener { _, isChecked ->
                val subList = listItem.find { it.uuid == subList.uuid }
                subList?.boolean = isChecked
            }
        }
    }
}