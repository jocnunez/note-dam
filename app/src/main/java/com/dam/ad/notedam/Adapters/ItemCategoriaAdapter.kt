package com.dam.ad.notedam.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dam.ad.notedam.Models.Categoria
import com.dam.ad.notedam.R
import com.dam.ad.notedam.databinding.ItemCategoriasBinding

class ItemCategoriaAdapter(
    private var listItem: MutableList<Categoria>,
    private var listener: ItemOnClickListener<Categoria>,
    private val onArrowUpClick: (Int) -> Unit,
    private val onArrowDownClick: (Int) -> Unit
) : RecyclerView.Adapter<ItemCategoriaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_categorias, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listItem.count()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCategorias (categorias : MutableList<Categoria>) {
        listItem = categorias
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        listItem.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItem[position]
        holder.bind(item)
        holder.setListener(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val binding = ItemCategoriasBinding.bind(view)

        fun bind(categoria: Categoria) {
            binding.nombreCategoria.text = categoria.nombreCategoria
            binding.imageViewArrowUp.setImageResource(R.drawable.baseline_keyboard_arrow_up_24)
            binding.imageViewArrowDown.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)

            binding.imageViewArrowUp.setOnClickListener {
                onArrowUpClick(adapterPosition)
            }

            binding.imageViewArrowDown.setOnClickListener {
                onArrowDownClick(adapterPosition)
            }
        }

        fun setListener(categoria: Categoria) {
            binding.root.apply {
                setOnClickListener {
                    listener.onClick(categoria.uuid)
                }
                setOnLongClickListener {
                    listener.onLongClickListener(categoria.uuid)
                    true
                }
            }


        }


    }
}
