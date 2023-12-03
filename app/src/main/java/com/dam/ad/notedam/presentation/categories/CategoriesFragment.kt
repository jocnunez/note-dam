package com.dam.ad.notedam.presentation.categories

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.domain.models.Categoria
import com.dam.ad.notedam.databinding.FragmentCategoriesBinding
import com.dam.ad.notedam.databinding.FragmentCategoryDataBinding
import com.dam.ad.notedam.presentation.adapter.CategoriesAdapter
import com.dam.ad.notedam.presentation.adapter.OnCategoryClickListener
import com.dam.ad.notedam.presentation.home.MainActivity
import java.util.*

class CategoriesFragment : Fragment(), OnCategoryClickListener {

    private lateinit var binding: FragmentCategoriesBinding

    private lateinit var mActivity: MainActivity
    private lateinit var mAdapter: CategoriesAdapter
    private lateinit var mLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = requireActivity() as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initBindings()

        setUpAdapter()

        mActivity.sharedViewModel.getCategoriesLiveData().observe(viewLifecycleOwner) { updatedList ->
            // Actualizar el RecyclerView
            mAdapter.setList(updatedList)
        }
    }

    /**
     * función usada para iniciar los bindings del fragmento actual
     * @author IvánRoncoCebadera
     */
    private fun initBindings() {
        binding.botonAAdirCategoria.setOnClickListener {
            createCategory(null)
        }
    }

    /**
     * función usada para iniciar el adaptador y el recylcerView de la vista de este fragmento
     * @author IvánRoncoCebadera
     */
    private fun setUpAdapter() {
        mAdapter = CategoriesAdapter(
            mActivity.sharedViewModel.getCategories(), this
        )

        mLayoutManager = LinearLayoutManager(binding.root.context)

        binding.listaCategorias.apply {
            setHasFixedSize(true)
            adapter = mAdapter
            layoutManager = mLayoutManager
        }
    }

    /**
     * función usada para abrir una ventana modal de edición/creación de una categoría
     * @author IvánRoncoCebadera
     * @param categoriaSeleccionada en caso de que se este editando tendrá los valores previos de la categoría elegida, en caso contrario, valdrá null
     */
    private fun createCategory(categoriaSeleccionada: Categoria?) {
        val builder = AlertDialog.Builder(requireContext())
        val bindingFragmentoCategorias = FragmentCategoryDataBinding.inflate(mActivity.layoutInflater)

        if (categoriaSeleccionada != null) {
            bindingFragmentoCategorias.textoNombreCategoria.setText(categoriaSeleccionada.nombre)
            bindingFragmentoCategorias.textoPrioridadCategoria.setText(categoriaSeleccionada.prioridad.toString())
            bindingFragmentoCategorias.checkBox.isChecked = categoriaSeleccionada.isChecked
        }

        builder.setView(bindingFragmentoCategorias.root)
            .setTitle("¡¡Escribe los datos de la categoría!!")
            .setPositiveButton("Aceptar") { _, _ ->
                try {
                    createAndLoadCategory(bindingFragmentoCategorias, categoriaSeleccionada)
                } catch (e: Exception) {
                    Toast.makeText(mActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar") { _, _ ->
                Toast.makeText(mActivity, "¡¡Se cancela la operación!!", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    /**
     * función que crea y cargar los datos de una categoría, tanto como si es nueva como si se trata de una edición
     * @author IvánRoncoCebadera
     * @param bindingFragmentoCategorias el binding de la vista de la que sacaremos los datos para la creación de la categoría
     * @param categoriaSeleccionada en caso de que se este editando tendrá los valores previos de la categoría elegida, en caso contrario, valdrá null
     */
    private fun createAndLoadCategory(
        bindingFragmentoCategorias: FragmentCategoryDataBinding,
        categoriaSeleccionada: Categoria?
    ) {
        val nombre = bindingFragmentoCategorias.textoNombreCategoria.text.toString()
        if (nombre.isEmpty()) {
            throw Exception("¡¡El nombre de la categoria no puede estar vacio!!")
        } else {
            if (!validarNombreCategoriaNoRepetido(nombre) && !nombre.lowercase().trim()
                    .equals(categoriaSeleccionada?.nombre?.lowercase()?.trim() ?: "")
            ) {
                throw Exception("¡¡El nombre de la categoria no puede estar repetido (cambiar mayusculas no vale)!!")
            }
        }
        val prioridad = Integer.parseInt(bindingFragmentoCategorias.textoPrioridadCategoria.text.toString())
        if (prioridad <= 0) {
            throw Exception("¡¡La prioridad siempre deberá ser mayor que 0!!")
        } else {
            if (prioridad > 9) {
                throw Exception("¡¡La prioridad siempre deberá ser menor que 9!!")
            }
        }
        val seleccionada = bindingFragmentoCategorias.checkBox.isChecked
        if (categoriaSeleccionada != null) {
            //Si estoy editando
            val categoria = Categoria(
                id = categoriaSeleccionada.id,
                nombre = nombre,
                prioridad = prioridad,
                isChecked = seleccionada,
                notas = categoriaSeleccionada.notas
            )
            mActivity.sharedViewModel.editCategoryLiveData(categoria)
        } else {
            val categoria =
                Categoria(nombre = nombre, prioridad = prioridad, isChecked = seleccionada, notas = mutableListOf())
            mActivity.sharedViewModel.addCategoryLiveData(categoria)
        }

        mActivity.exportBySourceData()
    }

    /**
     * función usada para comprobar si el nombre de una categoría se está repitiendo o no
     * @author IvánRoncoCebadera
     * @param nombreCategoria el nombre a validar
     */
    private fun validarNombreCategoriaNoRepetido(nombreCategoria: String): Boolean {
        mAdapter.getItems().forEach {
            if (nombreCategoria.lowercase().trim().equals(it.nombre.lowercase().trim())) {
                return false
            }
        }
        return true
    }

    /**
     * función usada para abrir una ventana modal para editar los datos de una categoría
     * @author IvánRoncoCebadera
     * @param categoria la categoría que se trata de editar
     */
    override fun editCategory(categoria: Categoria) {
        createCategory(categoria)
    }

    /**
     * función usada para tratar de borrar la categoría elegida, siempre que se encuentre/exista
     * @author IvánRoncoCebadera
     * @param categoryId el id por el que se buscará la categoría a eliminar
     */
    override fun deleteCategory(categoryId: UUID) {
        AlertDialog.Builder(binding.root.context).apply {
            setTitle("¿Seguro que quieres eliminar este registro?")
            setPositiveButton("Aceptar") { _, _ -> mActivity.sharedViewModel.deleteCategoryLiveData(categoryId) }
            setNegativeButton("Cancelar") { _, _ ->
                Toast.makeText(
                    binding.root.context,
                    "Se ha cancelado la operación!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }.create().show()
    }

    /**
     * función usada para tratar de actualizar la categoría recien seleccionada, siempre que se cumplan los requisitos
     * @author IvánRoncoCebadera
     * @param categoria la nueva categoría que se trata de seleccionar o deseleccionar
     */
    override fun editSelectedCategory(categoria: Categoria) {
        mActivity.sharedViewModel.editCategoryLiveData(categoria)
    }
}
