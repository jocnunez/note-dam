package com.dam.ad.notedam.presentation.todos

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dam.ad.notedam.R
import com.dam.ad.notedam.adapters.ItemCategoryAdapter
import com.dam.ad.notedam.adapters.ItemNoteAdapter
import com.dam.ad.notedam.adapters.OnElementClickListener
import com.dam.ad.notedam.databinding.FragmentTodosBinding
import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.Note
import com.dam.ad.notedam.models.State
import com.dam.ad.notedam.presentation.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class TodosFragment : Fragment(), OnElementClickListener<Note<*>> {

    private var _binding: FragmentTodosBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter: ItemNoteAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var recycler: RecyclerView

    private lateinit var state: State
    private lateinit var nav: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodosBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        state = (requireActivity() as MainActivity).getState() //Cogemos la instancia del estado en el main
        nav = (requireActivity() as MainActivity).getNav()

        setRecyclerView()

        nav.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.todosFragment) {
                state.categoryController.getCategorySelected()?.notes?.toMutableList()?.let { mAdapter.setNotes(it) }
            }
        }
    }

    private fun setRecyclerView() {

        val list = state.categoryController.getCategorySelected()?.notes?.toMutableList() ?: mutableListOf()

        mAdapter = ItemNoteAdapter(list, this)
        mLayoutManager = LinearLayoutManager(requireContext())

        recycler.apply {
            layoutManager = mLayoutManager
            adapter = mAdapter
        }
    }

    private fun addNote(note: Note<*>) {
        state.categoryController.addNoteToSelectedCategory(note)
        mAdapter.add(note)
    }

    override fun onClick(item: Note<*>) {
        state.noteSelected = item
    }

    override fun onLongClick(item: Note<*>) {

        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Alerta")
            .setMessage("¿Estás seguro de que deseas borrar la nota ${item.value}?")

        builder.setPositiveButton("Sí") { _, _ ->
            state.categoryController.getCategorySelected()?.notes?.toMutableList()?.remove(item)
            mAdapter.delete(item)
        }

        builder.setNegativeButton("No") { _, _ ->

        }

        builder.create().show()
    }

}