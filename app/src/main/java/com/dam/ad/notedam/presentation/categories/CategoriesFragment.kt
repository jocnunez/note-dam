package com.dam.ad.notedam.presentation.categories

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
import com.dam.ad.notedam.adapters.OnElementClickListener
import com.dam.ad.notedam.databinding.FragmentCategoriesBinding
import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.State
import com.dam.ad.notedam.presentation.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class CategoriesFragment : Fragment(), OnElementClickListener<Category> {

    private var _binding:FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter: ItemCategoryAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var recycler: RecyclerView

    private lateinit var state: State
    private lateinit var nav: NavController

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        state = (requireActivity() as MainActivity).getState() //Cogemos la instancia del estado en el main
        nav = (requireActivity() as MainActivity).getNav()
        setRecyclerView()
    }

    private fun setRecyclerView() {

        val list = state.categoryController.findAll().toMutableList()

        mAdapter = ItemCategoryAdapter(list, this)
        mLayoutManager = LinearLayoutManager(requireContext())

        recycler.apply {
            layoutManager = mLayoutManager
            adapter = mAdapter
        }
    }

    private fun addCategory(category: Category) {
        state.categoryController.save(category)
        mAdapter.add(category)
    }

    private fun sortCartegories() {
        mAdapter.setCategories(state.categoryController.findAll().sortedBy { it.priority }.toMutableList())
    }

    override fun onClick(item: Category) {
        state.categoryController.setCategorySelected(item)
        nav.navigate(R.id.todosFragment)
    }

    override fun onLongClick(item: Category) {

        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Alerta")
            .setMessage("¿Estás seguro de que deseas borrar la categoría ${item.title}?")

        builder.setPositiveButton("Sí") { _, _ ->
            state.categoryController.delete(item)
            mAdapter.delete(item)
        }

        builder.setNegativeButton("No") { _, _ ->

        }

        builder.create().show()
    }

}