package com.dam.ad.notedam.presentation.categories

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dam.ad.notedam.R
import com.dam.ad.notedam.adapters.ItemCategoryAdapter
import com.dam.ad.notedam.adapters.OnElementClickListener
import com.dam.ad.notedam.databinding.FragmentCategoriesBinding
import com.dam.ad.notedam.dialogs.CategoryAlertDialog
import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.State
import com.dam.ad.notedam.presentation.home.MainActivity
import com.github.michaelbull.result.onFailure
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class CategoriesFragment : Fragment(), OnElementClickListener<Category> {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter: ItemCategoryAdapter
    private lateinit var mLayoutManager: LinearLayoutManager

    private lateinit var state: State
    private lateinit var nav: NavController

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(layoutInflater, container, false)
        setRecyclerView()
        nav = (requireActivity() as MainActivity).getNav()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        state = (requireActivity() as MainActivity).getState() //Cogemos la instancia del estado en el main
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.anadirCategoryButton.setOnClickListener {
            CategoryAlertDialog.showAlertDialog(requireContext(), object: CategoryAlertDialog.CategoryAlertListener {
                override fun onPositiveButtonClick(text1: String, text2: String, text3: String) {
                    val category = Category(UUID.randomUUID(), text1, text2, text3.toUIntOrNull() ?: 0u, mutableListOf())
                    mAdapter.add(category)
                    state.categoryController.save(category)
                    sortCartegories()
                }

                override fun onNegativeButtonClick() {
                }
            }, "", "", "")
        }
    }

    private fun setRecyclerView() {

        val list = state.categoryController.findAll().sortedBy { it.priority }.toMutableList()

        mAdapter = ItemCategoryAdapter(list, this, state)
        mLayoutManager = LinearLayoutManager(requireContext())

        binding.recyclerCategory.apply {
            layoutManager = mLayoutManager
            adapter = mAdapter
        }
    }

    private fun sortCartegories() {
        mAdapter.setCategories(state.categoryController.findAll().sortedBy { it.priority }.toMutableList())
    }

    override fun onClick(item: Category) {
        state.categoryController.setCategorySelected(item)
        nav.navigate(R.id.todosFragment)
    }

    override fun onLongClick(item: Category) {

        val builder1 = AlertDialog.Builder(requireContext())
            .setItems(
                arrayOf("Editar", "Eliminar")
            ) { dialog, which ->
                if (which == 0) {
                    CategoryAlertDialog.showAlertDialog(
                        requireContext(),
                        object : CategoryAlertDialog.CategoryAlertListener {
                            override fun onPositiveButtonClick(
                                text1: String,
                                text2: String,
                                text3: String
                            ) {
                                item.title = text1
                                item.description = text2
                                item.priority = text3.toUInt()

                                state.categoryController.save(item)
                                mAdapter.setCategories(state.categoryController.findAll().sortedBy { it.priority }.toMutableList())
                            }

                            override fun onNegativeButtonClick() {
                            }
                        },
                        item.title,
                        item.description,
                        item.priority.toString()
                    )
                } else {
                    val builder2 = AlertDialog.Builder(requireContext())

                    builder2.setTitle("Alerta")
                        .setMessage("¿Estás seguro de que deseas borrar la categoría ${item.title}?")

                    builder2.setPositiveButton("Sí") { _, _ ->
                        state.categoryController.delete(item)
                        mAdapter.delete(item)
                    }

                    builder2.setNegativeButton("No") { _, _ ->

                    }

                    builder2.create().show()
                }
            }
        builder1.create().show()

    }
}