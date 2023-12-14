package com.dam.ad.notedam.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dam.ad.notedam.Models.Categoria
import com.dam.ad.notedam.Models.nota.Nota
import com.dam.ad.notedam.Models.nota.NotaLista
import com.dam.ad.notedam.Models.nota.SubList
import com.dam.ad.notedam.R
import com.dam.ad.notedam.databinding.ItemCategoriasBinding
import com.dam.ad.notedam.databinding.ItemSublistaBinding
import java.util.*

class SubListAdapter (
    private var listItem: MutableList<SubList>,
    private val onSubListChangedListener: ((MutableList<SubList>) -> Unit)? = null
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

    private fun notifySubListChanged() {
        onSubListChangedListener?.invoke(listItem)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val binding = ItemSublistaBinding.bind(view)

        fun bind(subList: SubList) {
            binding.checkBoxElement.text = subList.texto
            binding.checkBoxElement.isChecked = subList.boolean

            binding.checkBoxElement.setOnCheckedChangeListener(null)
            binding.checkBoxElement.setOnCheckedChangeListener { _, isChecked ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listItem[position].boolean = isChecked
                }
                notifySubListChanged()
            }
        }

        fun setListener(subList: SubList) {
            binding.checkBoxElement.setOnLongClickListener {
                removeElement(subList.uuid)
                notifySubListChanged()
                true
            }
        }
    }
}