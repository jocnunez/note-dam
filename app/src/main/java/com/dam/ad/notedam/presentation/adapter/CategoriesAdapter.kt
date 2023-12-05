package com.dam.ad.notedam.presentation.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.core.domain.models.Categoria
import com.dam.ad.notedam.R
import com.dam.ad.notedam.databinding.CategoryViewBinding

class CategoriesAdapter(
    private var listaCategorias: List<Categoria>,
    private val listener: OnCategoryClickListener
) : RecyclerView.Adapter<CategoriesAdapter.CategoriesAdapterViewHolder>() {

    /**
     * función usada para devolver todas las categorías del adapter
     * @author IvánRoncoCebadera
     * @return una List<Categoria>
     */
    fun getItems(): List<Categoria> {
        return listaCategorias
    }

    /**
     * función usada para establecer una nueva lista de categorías en el adapter
     * @author IvánRoncoCebadera
     * @param lista un List<Categoria> que se va establecer como la nueva lista de categorías
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setList(updatedList: List<Categoria>?) {
        if (updatedList != null) {
            listaCategorias = updatedList
            notifyDataSetChanged()
            Log.i("test", "Se ha cambiado la lista a: $listaCategorias")
        }
    }

    inner class CategoriesAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: CategoryViewBinding = CategoryViewBinding.bind(view)

        /**
         * función usada para iniciar los bindings del fragmento actual
         * @author IvánRoncoCebadera
         * @param categoria la categoría cuyos datos se representarán en la nota del recyclerView
         */
        @SuppressLint("ResourceAsColor")
        fun bind(categoria: Categoria) {
            binding.textoNombreCategoria.text = categoria.nombre

            binding.textoPrioridadCategoria.text = categoria.prioridad.toString()

            binding.textoNumeroNotasDeCategoria.text = categoria.notas.size.toString()

            binding.estaSeleccionado.isChecked = categoria.isChecked

            setUpListeners(categoria)
        }

        /**
         * función usada para iniciar los listener del fragmento actual
         * @author IvánRoncoCebadera
         * @param category la categoría cuyos datos se utilizaran para los listeners
         */
        private fun setUpListeners(category: Categoria) {
            binding.cartaCategoria.setOnLongClickListener {
                val popupMenu = PopupMenu(binding.root.context, it)
                popupMenu.menuInflater.inflate(R.menu.menu_categorias, popupMenu.menu)

                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.botonDesplegableEditar -> {
                            listener.editCategory(category)
                            true
                        }

                        R.id.botonDesplegableEliminar -> {
                            listener.deleteCategory(category.id)
                            true
                        }

                        else -> false
                    }
                }

                popupMenu.show()
                true
            }

            binding.estaSeleccionado.setOnClickListener {
                listener.editSelectedCategory(category.copy(isChecked = !category.isChecked))
            }
        }
    }

    override fun onBindViewHolder(holder: CategoriesAdapterViewHolder, position: Int) {
        val currentCategory = listaCategorias.get(position)

        with(holder) {
            bind(currentCategory)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.category_view,
            parent,
            false
        )
        return CategoriesAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaCategorias.size
    }
}
