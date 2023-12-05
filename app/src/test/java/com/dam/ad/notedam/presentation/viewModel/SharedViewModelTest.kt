package com.dam.ad.notedam.presentation.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.core.domain.models.Categoria
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class SharedViewModelTest {

    private val sharedViewModel = SharedViewModel()

    private val category1 = Categoria(nombre = "Comida", prioridad = 4, isChecked = false, notas = mutableListOf())
    private val category2 = Categoria(nombre = "Habitaciones", prioridad = 1, isChecked = true, notas = mutableListOf())

    private val categoryListValues = mutableListOf<Categoria>(
        category1, category2
    )

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore() {
        sharedViewModel.updateSharedData(category2)
        sharedViewModel.updateSharedData(category2.copy(isChecked = false))
        sharedViewModel.submitCategoriesLiveData(categoryListValues)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun updateSharedDataNewSelection() {
        val cat1Selected = category1.copy(isChecked = true)
        sharedViewModel.updateSharedData(cat1Selected)
        val gettedSharedData = sharedViewModel.getSharedData()

        assert(cat1Selected == gettedSharedData)
    }

    @Test
    fun updateSharedDataRemoveSelection() {
        sharedViewModel.updateSharedData(category2)
        sharedViewModel.updateSharedData(category2.copy(isChecked = false))
        val gettedSharedData = sharedViewModel.getSharedData()

        assert(null == gettedSharedData)
    }

    @Test
    fun updateSharedDataDeleteSelectedCategoryCategory() {
        sharedViewModel.updateSharedData(category2)
        sharedViewModel.submitCategoriesLiveData(mutableListOf(category1))
        sharedViewModel.updateSharedData(category2)
        val gettedSharedData = sharedViewModel.getSharedData()

        assert(null == gettedSharedData)
    }

    @Test
    fun updateSharedDataDeleteNotSelectedCategoryCategory() {
        sharedViewModel.updateSharedData(category2)
        sharedViewModel.submitCategoriesLiveData(mutableListOf(category2))
        sharedViewModel.updateSharedData(category1)
        val gettedSharedData = sharedViewModel.getSharedData()

        assert(category2 == gettedSharedData)
    }

    @Test
    fun getSharedData() {
        sharedViewModel.updateSharedData(category2)
        val gettedSharedData = sharedViewModel.getSharedData()

        assert(gettedSharedData == category2)
    }

    @Test
    fun submitCategoriesLiveData() {
        val newCat = Categoria(isChecked = true, prioridad = 1, nombre = "Teteras", notas = mutableListOf())
        val newCatList = categoryListValues + newCat
        sharedViewModel.submitCategoriesLiveData(newCatList)
        val catList = sharedViewModel.getCategories()

        assert(catList.size == newCatList.size)
        assert(catList.get(0) == category1)
        assert(catList.get(1) == category2)
        assert(catList.get(2) == newCat)
    }

    @Test
    fun getCategoriesLiveData() {
        val catList = sharedViewModel.getCategories()

        assert(catList == categoryListValues)
    }

    @Test
    fun editCategoryLiveData() {
        val editedCat = category1.copy(nombre = "Bebidas", isChecked = true)
        sharedViewModel.editCategoryLiveData(editedCat)
        val catList = sharedViewModel.getCategories()

        assert(catList.size == categoryListValues.size)
        assert(catList.get(0) == editedCat)
        assert(catList.get(1) == category2.copy(isChecked = false)) //Al seleccionar el otro, se deselecciona el que previamente estaba seleccionado
    }

    @Test
    fun addCategoryLiveData() {
        val newCat = Categoria(isChecked = true, prioridad = 1, nombre = "Teteras", notas = mutableListOf())
        sharedViewModel.addCategoryLiveData(newCat)
        val catList = sharedViewModel.getCategories()

        assert(catList.size == categoryListValues.size+1)
        assert(catList.get(0) == category1)
        assert(catList.get(1) == category2.copy(isChecked = false)) //Esto cambia, porque la nueva categoría es la seleccionada
        assert(catList.get(2) == newCat)
    }

    @Test
    fun deleteCategoryLiveData() {
        sharedViewModel.deleteCategoryLiveData(category2.id)
        val catList = sharedViewModel.getCategories()

        assert(catList.size == categoryListValues.size-1)
        assert(catList.get(0) == category1)
    }
}
