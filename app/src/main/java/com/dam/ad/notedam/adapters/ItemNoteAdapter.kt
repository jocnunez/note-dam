package com.dam.ad.notedam.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dam.ad.notedam.R
import com.dam.ad.notedam.databinding.ItemCategoryLayoutBinding
import com.dam.ad.notedam.databinding.ItemNoteLayoutBinding
import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.Note

class ItemNoteAdapter(private var listItem: MutableList<Note<*>>, private var listener: OnElementClickListener<Note<*>>) : RecyclerView.Adapter<ItemNoteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note_layout, parent, false)
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

        private val binding = ItemNoteLayoutBinding.bind(view)

        fun bind(item: Note<*>) {
            when (item) {
                is Note.Text -> {
//                    binding.completed = item.check
//                    binding.fecha = item.fechaCreate
//                    binding.value = item.text
                }
                is Note.Audio -> {
//                    binding.completed = item.check
//                    binding.fecha = item.fechaCreate
//                    binding.value = item.audio
                }
                is Note.Image -> {
//                    binding.completed = item.check
//                    binding.fecha = item.fechaCreate
//                    binding.value = item.image
                }
                is Note.Sublist -> {
//                    binding.completed = item.check
//                    binding.fecha = item.fechaCreate
//                    binding.value = item.sublist
                }
            }
        }

        fun setListener(item: Note<*>) {

            binding.root.setOnClickListener {
                listener.onClick(item)
            }
            binding.root.setOnLongClickListener {
                listener.onLongClick(item)
                true
            }
        }
    }

    fun setNotes(notes: MutableList<Note<*>>) {
        listItem = notes
        notifyDataSetChanged()
    }

    fun add(note: Note<*>) {
        listItem.add(note)
        notifyDataSetChanged()
    }

    fun delete(note: Note<*>) {
        val index = listItem.indexOf(note)
        if (index != -1) {
            listItem.removeAt(index)
            notifyItemRemoved(index)
        }
    }

}