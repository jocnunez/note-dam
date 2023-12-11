package com.dam.ad.notedam.presentation.todos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.dam.ad.notedam.adapter.RecyclerTodoAdapter
import com.dam.ad.notedam.databinding.FragmentTodosBinding
import com.dam.ad.notedam.listeners.RecyclerSublistOnClickListener
import com.dam.ad.notedam.listeners.RecyclerTodoOnClickListener
import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.SublistItem
import com.dam.ad.notedam.models.Todo
import com.dam.ad.notedam.services.storage.StorageManager
import com.dam.ad.notedam.services.storage.StorageManager.dataStore
import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID


@AndroidEntryPoint
class TodosFragment : Fragment(), RecyclerTodoOnClickListener, RecyclerSublistOnClickListener {
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
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
    }

    private fun setRecyclerView() {
        var selectedCategory: Category? = null
        lifecycleScope.launch {
            selectedCategory = readSelectedCategory()
        }

        binding.recyclerViewTodos.apply {
            adapter = selectedCategory?.todos?.let { RecyclerTodoAdapter(it, this@TodosFragment) }
            layoutManager = GridLayoutManager(context, 1)
        }
    }

    private suspend fun readSelectedCategory(): Category? {
        val key = stringPreferencesKey("selected_category")
        var selectedCategory: String? = null
        requireContext().dataStore.data.map { preferences ->
            preferences[key]
        }.collect { selectedCategory = it }

        return StorageManager.categories.find { it.uuid == UUID.fromString(selectedCategory) }

    }

    override fun onClick(todo: Todo) {
        todo.completed = !todo.completed
    }

    override fun onClick(sublistItem: SublistItem) {
        sublistItem.completed = !sublistItem.completed
    }
}
