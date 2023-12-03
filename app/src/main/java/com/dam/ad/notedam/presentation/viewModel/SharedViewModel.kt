package com.dam.ad.notedam.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.models.Categoria
import com.core.domain.models.Preferences
import java.util.*

class SharedViewModel : ViewModel() {
    private var sharedData: Categoria? = null

    private val categoryList: MutableLiveData<List<Categoria>> = MutableLiveData()

    private var actualDataSource: Preferences? = null

    /**
     * es una función un tanto compleja que gestiona cual de todas las categoría es la que está seleccionada, ya que solo puede haber una
     * @author IvánRoncoCebadera
     * @param newData es la nueva categoria seleccionada, o la categoría actual que se deselecciona
     */
    fun updateSharedData(newData: Categoria) {
        if (newData.isChecked && newData.id != sharedData?.id) { //Si se selecciona una nueva categoria
            sharedData = newData
            for (cat in categoryList.value ?: mutableListOf()) {
                if (cat.isChecked && cat.id != newData.id) {
                    editCategoryLiveData(cat.copy(isChecked = !cat.isChecked)) //En el caso de que otra categoría este seleccionada, la deselecciono
                    break
                }
            }
        } else {
            if (newData.id == (sharedData?.id
                    ?: -1) && !newData.isChecked
            ) { //Si se selecciona, el que ya estaba seleccionado
                sharedData = null
            } else {
                if (!(categoryList.value
                        ?: mutableListOf()).contains(newData) && newData.isChecked
                ) { //Si fue eliminado siendo el seleccionado
                    sharedData = null
                }
            }
        }
    }

    /**
     * recuperamos la categoría seleccionada o un nulo si no la hay
     * @author IvánRoncoCebadera
     * @return o la categoría actualmente seleccionada o un nulo
     */
    fun getSharedData(): Categoria? {
        return sharedData
    }

    /**
     * Necesito dejarlo en nulo cuando cambiamos el formato de dato (XMl, CSV, JSON, etc)
     * @author Angel Maroto Chivite
     */
    fun clearSharedData() {
        sharedData = null
    }

    /**
     * recive y coloco una nueva lista de categorías
     * @author IvánRoncoCebadera
     * @param categorias del tipo List<Categoria>, son las nuevas categorias a almacenar
     */
    fun submitCategoriesLiveData(categorias: List<Categoria>) {
        categoryList.value = categorias

        // Buscamos la categoría seleccionada
        if (sharedData == null) {
            for (category in categoryList.value ?: mutableListOf()) {
                if (category.isChecked) {
                    sharedData = category
                    break
                }
            }
        }
    }

    /**
     * devuelve la lista de categorías almacenadas
     * @author IvánRoncoCebadera
     * @return la lista de categorías almacenadas del tipo MutableLiveData<List<Categoria>>, en caso de que no hayan guardadas, las categorias son un null
     */
    fun getCategoriesLiveData(): MutableLiveData<List<Categoria>> {
        return categoryList
    }

    /**
     * recive los nuevos datos a asignar a una categoría. A la hora de recargar los datos de la lista, la categoría no cambía de la posición en la que se encontraba, previa al cambio. También se intenta cambiar la categoría seleccionada, por si acaso cumple con los requisitos.
     * @author IvánRoncoCebadera
     * @param category es la categoria que contiene los datos de la prevía categoría, pero actualizados
     */
    fun editCategoryLiveData(category: Categoria) {
        if (categoryList.value != null) {
            for (i in 0 until (categoryList.value?.size ?: 0)) {
                if (categoryList.value?.get(i)?.id!! == category.id) {
                    val newList = categoryList.value!!.toMutableList()
                    for (i in 0 until newList.size) {
                        if (newList.get(i).id == category.id) {
                            newList.set(i, category) //De esta forma puedo mantener la categoria en el mismo sitio
                            break
                        }
                    }
                    categoryList.value = newList.toList()
                    updateSharedData(category)
                    break
                }
            }
        }
    }

    /**
     * @author Angel Maroto Chivite
     * @param category con solamente las notas cambiadas
     */
    fun editNotesFromCategory(categoryUpdated: Categoria) {
        sharedData = categoryUpdated
        categoryList.value = categoryList.value?.filter { it.id != categoryUpdated.id }
        categoryList.value = categoryList.value?.plus(categoryUpdated)
        categoryList.value = categoryList.value?.sortedBy { it.prioridad }
    }

    /**
     * recive una nueva categoría a introducir en la lista. A la hora de recargar los datos de la lista, la categoría se coloca al final de la lista. También se intenta cambiar la categoría seleccionada, por si acaso cumple con los requisitos.
     * @author IvánRoncoCebadera
     * @param category es la nueva categoria que se va a introducir
     */
    fun addCategoryLiveData(category: Categoria) {
        if (categoryList.value != null) {
            categoryList.value = (categoryList.value!! + category)
            updateSharedData(category)
        }
    }

    /**
     * recive el id de una categoría, la cual queremos eliminar al completo. También se intenta cambiar la categoría seleccionada, por si acaso cumple con los requisitos.
     * @author IvánRoncoCebadera
     * @param categoryId es la identificación de la categoría que se quiere eliminar
     */
    fun deleteCategoryLiveData(categoryId: UUID) {
        if (categoryList.value != null) {
            for (i in 0 until (categoryList.value?.size ?: 0)) {
                if (categoryList.value?.get(i)?.id!! == categoryId) {
                    val category = categoryList.value?.get(i)!!
                    categoryList.value = categoryList.value!!.filter { it.id != category.id }.toList()
                    updateSharedData(category)
                    break
                }
            }
        }
    }

    fun getCategories(): List<Categoria> {
        if (categoryList.value.isNullOrEmpty()) {
            categoryList.value = listOf()
        }
        return categoryList.value as List<Categoria>
    }

    /**
     * Encapsulación para establecer el nuevo valor a la propiedad actualDataSource
     * @author Angel Maroto Chivite
     * @param dataSource es el nuevo valor a asignar
     */
    fun setActualDataSource(dataSource: Preferences) {
        actualDataSource = dataSource
    }

    /**
     * Encapsulación para recoger el valor de la propiedad actualDataSource
     * @author Angel Maroto Chivite
     * @return actualDataSource
     */
    fun getActualDataSource(): Preferences? {
        return actualDataSource
    }
}
