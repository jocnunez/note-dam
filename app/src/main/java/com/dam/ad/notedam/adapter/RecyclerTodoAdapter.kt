package com.dam.ad.notedam.adapter

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dam.ad.notedam.R
import com.dam.ad.notedam.databinding.ItemTodoBinding
import com.dam.ad.notedam.listeners.RecyclerSublistOnClickListener
import com.dam.ad.notedam.listeners.RecyclerTodoOnClickListener
import com.dam.ad.notedam.models.Todo

class RecyclerTodoAdapter(
    private val listTodo: MutableList<Todo>,
    private val listener: RecyclerTodoOnClickListener,
    private val listenerSublist: RecyclerSublistOnClickListener
) : RecyclerView.Adapter<RecyclerTodoAdapter.ViewHolder>() {

    private var audioAttributes = AudioAttributes.Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .build()

    private val mediaPlayer = MediaPlayer().apply {
        setAudioAttributes(audioAttributes)
        setOnCompletionListener {
            stop()
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemTodoBinding.bind(view)

        fun bind(todo: Todo) {
            binding.textViewTitle.text = todo.title
            binding.textViewFechaCreacion.text = "Fecha de creación: ${todo.createdAt}"
            binding.textViewCompletada.isChecked = todo.completed
            when (todo) {
                is Todo.TextTodo -> {
                    binding.textViewTexto.isVisible = true
                    binding.textViewTexto.text = todo.text
                }

                is Todo.AudioTodo -> {
                    binding.buttonReproducirAudio.isVisible = true
                    binding.buttonReproducirAudio.setOnClickListener {
                        if (mediaPlayer.isPlaying) {
                            mediaPlayer.stop()
                        } else {
                            mediaPlayer.reset()
                            mediaPlayer.setDataSource(todo.audio)
                            mediaPlayer.prepare()
                            mediaPlayer.start()
                        }
                    }
                }

                is Todo.ImageTodo -> {
                    binding.imageViewImagen.isVisible = true
                    Glide.with(binding.imageViewImagen)
                        .load(todo.image)
                        .into(binding.imageViewImagen)
                }

                is Todo.SublistTodo -> {
                    binding.listViewSublist.isVisible = true
                    binding.listViewSublist.adapter = ArrayAdapter(
                        binding.root.context,
                        android.R.layout.simple_list_item_1,
                        todo.sublist
                    )
                }
            }
        }

        fun setListener(todo: Todo) {
            binding.root.setOnClickListener {
                listener.onClick(todo)
                binding.textViewCompletada.isChecked = !binding.textViewCompletada.isChecked
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listTodo[position])
        holder.setListener(listTodo[position])
    }


    override fun getItemCount(): Int {
        return listTodo.count()
    }


}
