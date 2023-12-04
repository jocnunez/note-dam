package com.dam.ad.notedam.adapters

import android.app.AlertDialog
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dam.ad.notedam.R
import com.dam.ad.notedam.databinding.ActivityMainBinding
import com.dam.ad.notedam.databinding.ItemAudioLayoutBinding
import com.dam.ad.notedam.databinding.ItemImageLayoutBinding
import com.dam.ad.notedam.databinding.ItemSublistLayoutBinding
import com.dam.ad.notedam.databinding.ItemTextLayoutBinding
import com.dam.ad.notedam.dialogs.NoteAlertDialog
import com.dam.ad.notedam.models.Note
import com.dam.ad.notedam.models.SublistItem
import com.dam.ad.notedam.presentation.home.MainActivity
import java.io.File
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class ItemNoteAdapter(private var listItem: MutableList<Note<*>>, private var listener: OnElementClickListener<Note<*>>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_text_layout, parent, false)
                TextNoteViewHolder(view)
            }

            1 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_image_layout, parent, false)
                ImageNoteViewHolder(view)
            }

            2 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_audio_layout, parent, false)
                AudioNoteViewHolder(view)
            }

            3 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_sublist_layout, parent, false)
                SublistNoteViewHolder(view)
            }

            else -> throw IllegalArgumentException("Tipo de vista desconocido")
        }
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (listItem[position]) {
            is Note.Text -> 0
            is Note.Image -> 1
            is Note.Audio -> 2
            is Note.Sublist -> 3
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = listItem[position]
        when (holder.itemViewType) {
            0 -> {
                val textHolder = holder as TextNoteViewHolder
                textHolder.bind(item as Note.Text)
                textHolder.setListener(item)
            }
            1 -> {
                val imageHolder = holder as ImageNoteViewHolder
                imageHolder.bind(item as Note.Image)
                imageHolder.setListener(item)
            }
            2 -> {
                val audioHolder = holder as AudioNoteViewHolder
                audioHolder.bind(item as Note.Audio)
                audioHolder.setListener(item)
            }
            3 -> {
                val sublistHolder = holder as SublistNoteViewHolder
                sublistHolder.bind(item as Note.Sublist)
                sublistHolder.setListener(item)
            }
        }
    }

    inner class TextNoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemTextLayoutBinding.bind(view)

        fun bind(item: Note.Text) {
            binding.text.text = item.text
            binding.date.text = item.fechaCreate.format(DateTimeFormatter.ofPattern("dd/MM/yy"))
            binding.checkbox.isChecked = item.check
        }

        fun setListener(item: Note.Text) {

            binding.root.setOnClickListener {
                listener.onClick(item)
            }
            binding.root.setOnLongClickListener {
                listener.onLongClick(item)
                true
            }
        }
    }

    inner class ImageNoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemImageLayoutBinding.bind(view)

        fun bind(item: Note.Image) {
            try {
                Glide.with(binding.image)
                    .load(item.image.toString())
                    .transform(CenterCrop(), RoundedCorners(30))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.image)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            binding.date.text = item.fechaCreate.format(DateTimeFormatter.ofPattern("dd/MM/yy"))
            binding.checkbox.isChecked = item.check
        }

        fun setListener(item: Note.Image) {

            binding.root.setOnClickListener {
                listener.onClick(item)
            }
            binding.root.setOnLongClickListener {
                listener.onLongClick(item)
                true
            }
        }
    }

    inner class AudioNoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemAudioLayoutBinding.bind(view)

        fun bind(item: Note.Audio) {
            binding.link.text = item.audio.toString()
            binding.date.text = item.fechaCreate.format(DateTimeFormatter.ofPattern("dd/MM/yy"))
            binding.checkbox.isChecked = item.check
        }

        fun setListener(item: Note.Audio) {

            binding.root.setOnClickListener {
                listener.onClick(item)
            }
            binding.root.setOnLongClickListener {
                listener.onLongClick(item)
                true
            }
        }
    }

    inner class SublistNoteViewHolder(view: View) : RecyclerView.ViewHolder(view), OnElementClickListener<SublistItem> {

        private val binding = ItemSublistLayoutBinding.bind(view)
        private lateinit var mAdapter: ItemSublistAdapter

        fun bind(item: Note.Sublist) {
            binding.date.text = item.fechaCreate.format(DateTimeFormatter.ofPattern("dd/MM/yy"))
            binding.checkbox.isChecked = item.check

            binding.anadir.setOnClickListener {
                NoteAlertDialog.showElementAlertDialog(
                    itemView.context,
                    object : NoteAlertDialog.NoteAlertListener {
                        override fun onPositiveButtonClick(
                            text: String
                        ) {

                            val sublistItem = SublistItem(false, text)
                            item.sublist.toMutableList().add(sublistItem)
                            mAdapter.add(sublistItem)

                        }

                        override fun onNegativeButtonClick() {
                        }
                    },
                    ""
                )
            }

            val list = item.sublist.toMutableList()
            mAdapter = ItemSublistAdapter(list, this)

            binding.sublistRecycler.apply {
                layoutManager = LinearLayoutManager(binding.root.context)
                adapter = mAdapter
            }

        }

        fun setListener(item: Note.Sublist) {

            binding.root.setOnClickListener {
                listener.onClick(item)
            }
            binding.root.setOnLongClickListener {
                listener.onLongClick(item)
                true
            }
        }

        override fun onClick(item: SublistItem) {

        }

        override fun onLongClick(item: SublistItem) {
            val builder = AlertDialog.Builder(binding.root.context)

            builder.setTitle("Alerta")
                .setMessage("¿Estás seguro de que deseas borrar esta tarea?")

            builder.setPositiveButton("Sí") { _, _ ->
//                state.categoryController.delete(item)
                mAdapter.delete(item)
            }

            builder.setNegativeButton("No") { _, _ ->

            }

            builder.create().show()
        }
    }

    fun setNotes(notes: MutableList<Note<*>>) {
        listItem = notes
        notifyDataSetChanged()
    }

    fun add(note: Note<*>) {
        listItem.add(note)
        notifyItemInserted(listItem.size - 1)
    }

    fun delete(note: Note<*>) {
        val index = listItem.indexOf(note)
        if (index != -1) {
            listItem.removeAt(index)
            notifyItemRemoved(index)
        }
    }

}