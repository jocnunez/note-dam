package com.dam.ad.notedam.presentation.todos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.dam.ad.notedam.adapter.RecyclerTodoAdapter
import com.dam.ad.notedam.databinding.FragmentTodosBinding
import com.dam.ad.notedam.listeners.RecyclerSublistOnClickListener
import com.dam.ad.notedam.listeners.RecyclerTodoOnClickListener
import com.dam.ad.notedam.models.SublistItem
import com.dam.ad.notedam.models.Todo
import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.flow.map
import java.time.LocalDate


@AndroidEntryPoint
class TodosFragment : Fragment(), RecyclerTodoOnClickListener, RecyclerSublistOnClickListener {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
    private var _binding: FragmentTodosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodosBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val listaTodosEjemplo = mutableListOf<Todo>(
            Todo.TextTodo(
                title = "Todo 1",
                createdAt = LocalDate.now(),
                completed = true,
                text = "Todo 1"
            ),
            Todo.AudioTodo(
                title = "Todo 2",
                createdAt = LocalDate.now(),
                completed = false,
                audio = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
            ),
            Todo.ImageTodo(
                title = "Todo 3",
                createdAt = LocalDate.now(),
                completed = false,
                image = "https://www.gstatic.com/webp/gallery/1.jpg"
            ),
            Todo.SublistTodo(
                title = "Todo 4",
                createdAt = LocalDate.now(),
                completed = false,
                sublist = mutableListOf(
                    SublistItem(
                        title = "Todo 4.1",
                        completed = false
                    ),
                    SublistItem(
                        title = "Todo 4.2",
                        completed = true
                    )
                )
            )
            )
        binding.recyclerViewTodos.apply {
            adapter = RecyclerTodoAdapter(listaTodosEjemplo, this@TodosFragment, this@TodosFragment)
            layoutManager = GridLayoutManager(context, 1)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private suspend fun readSelectedCategory(): String? {
        val key = stringPreferencesKey("selected_category")
        var selectedCategory: String? = null
        requireContext().dataStore.data.map { preferences ->
            preferences[key]
        }.collect { selectedCategory = it }
        return selectedCategory
    }

    override fun onClick(todo: Todo) {
        todo.completed = !todo.completed
    }

    override fun onClick(sublistItem: SublistItem) {
        sublistItem.completed = !sublistItem.completed
    }
}
