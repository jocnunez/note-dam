package com.dam.ad.notedam.presentation.categories

import android.R.layout.simple_spinner_item
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.dam.ad.notedam.databinding.FragmentCategoriesBinding
import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.services.storage.StorageManager
import com.dam.ad.notedam.services.storage.StorageManager.dataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

@AndroidEntryPoint
class CategoriesFragment : Fragment(), CreateCategoryFragment.OnInputListener {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoriesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSpinner()
        setButton()
    }

    private fun setSpinner() {
        binding.spinnerCategories.adapter = ArrayAdapter(
            requireContext(),
            simple_spinner_item,
            StorageManager.categories
        )

        binding.spinnerCategories.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedCategory = parent?.getItemAtPosition(position) as Category
                    lifecycleScope.launch { writeSelectedCategory(selectedCategory.uuid) }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    /* nothing */
                }
            }
    }


    private fun setButton() {
        binding.btnAdd.setOnClickListener {
            val dialog = CreateCategoryFragment()
            dialog.setOnInputListener(this)
            dialog.show(parentFragmentManager, "createCategory")
        }
    }

    private suspend fun writeSelectedCategory(categoryUuid: UUID) {
        val selectedCategoryKey = stringPreferencesKey("selected_category")
        requireContext().dataStore.edit { it[selectedCategoryKey] = categoryUuid.toString() }
        println("writeSelectedCategory: $categoryUuid")
    }

    override fun onInputConfirmed(catName: String) {
        StorageManager.categories.add(
            Category(
                UUID.randomUUID(),
                catName,
                LocalDate.now(),
                mutableListOf()
            )
        )

        println("onInputConfirmed: $catName")

        binding.spinnerCategories.adapter = ArrayAdapter(
            requireContext(),
            simple_spinner_item,
            StorageManager.categories
        )
    }
}
