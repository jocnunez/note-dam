package com.dam.ad.notedam.Fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.os.ParcelUuid
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavHostController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dam.ad.notedam.Activities.MainActivity
import com.dam.ad.notedam.Adapters.ItemCategoriaAdapter
import com.dam.ad.notedam.Adapters.ItemOnClickListener
import com.dam.ad.notedam.Config.ConfigFileType
import com.dam.ad.notedam.Config.ConfigStorageType
import com.dam.ad.notedam.Enums.StorageType
import com.dam.ad.notedam.Models.Categoria
import com.dam.ad.notedam.R
import com.dam.ad.notedam.databinding.FragmentCategoriesBinding
import com.dam.ad.notedam.repositories.CategoriaRepository
import com.dam.ad.notedam.repositories.ICategoriaRepository
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class CategoriesFragment() : Fragment() , ItemOnClickListener<Categoria>{

    private var _binding:FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository : ICategoriaRepository
    private lateinit var mAdapter : ItemCategoriaAdapter
    private lateinit var mLayoutManager : LinearLayoutManager
    var lista: MutableList<Categoria> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentCategoriesBinding.inflate(layoutInflater, container, false)


        setRecyclerView()
        binding.addButton.setOnClickListener {
            addNewCategoria()
        }

        return binding.root
    }

    private fun addNewCategoria() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Nueva categoria")

        val input = EditText(requireContext())
        builder.setView(input)

        builder.setPositiveButton("Guardar") { _, _ ->
            Log.i("CategoriesFragment" , "Guardando")
            repository.addItem(
                Categoria(
                    nombreCategoria = input.text.toString(),
                    prioridadCategoria = lista.size
                )
            )

            mAdapter.notifyDataSetChanged()

        }

        builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.cancel() }

        builder.show()
    }


    private fun setRecyclerView() {
        // Cargar Lista
        repository = CategoriaRepository(activity as MainActivity)
        lista = repository.loadAllItems().component1()!!

        // Crear el adaptador y configurar el RecyclerView
        mAdapter = ItemCategoriaAdapter(lista, this,
            onArrowUpClick = { position : Int ->
                if (position > 0) {
                    val item = lista.removeAt(position)
                    item.subirCategoria();
                    lista.add(position - 1, item)
                    lista[position].bajarCategoria()
                    mAdapter.notifyItemMoved(position, position - 1)
                    lista.forEach {
                        repository.updateItem(it)
                    }
                }

            },
           onArrowDownClick = { position ->
               if (position < lista.size - 1) {
                   val item = lista.removeAt(position)
                   item.bajarCategoria();
                   lista.add(position + 1, item)
                   lista[position].subirCategoria()
                   mAdapter.notifyItemMoved(position, position + 1)
                   lista.forEach {
                       repository.updateItem(it)
                   }
               }
        })
        mLayoutManager = LinearLayoutManager(requireContext())

        binding.categoriasRecyclerView.apply {
            layoutManager = mLayoutManager
            adapter = mAdapter
        }
    }



    override fun onClick(uuid: UUID) {
        abrirCategoria(uuid)
    }



    override fun onLongClickListener(uuid: UUID): Boolean {

        val items = arrayOf("Actualizar", "Eliminar")

            AlertDialog.Builder(requireContext())
                .setTitle("Selecciona una opción")
                .setItems(items) { dialog, which ->
                    when (which) {
                        0 -> onUpdate(uuid)
                        1 -> onDelete(uuid)
                    }
                    dialog.dismiss()
                }
                    .show()


        return true
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onUpdate(uuid: UUID) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Cambiar nombre de categoría")

        // Obtener la categoría actual basada en el UUID
        val categoria = lista.find { it.uuid == uuid }!!

        val input = EditText(requireContext())
        input.setText(categoria.nombreCategoria) // Establecer el texto actual en el cuadro de texto

        builder.setView(input)

        builder.setPositiveButton("Guardar") { _, _ ->
            val nuevoNombre = input.text.toString()

            categoria.nombreCategoria = nuevoNombre

            repository.updateItem(categoria)

            mAdapter.notifyDataSetChanged()

            Toast.makeText(requireContext(), "Nombre actualizado", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun onDelete(uuid: UUID) {
        Log.d("CategoriaAdapter", "onDelete: $uuid")
        val posicion = lista.indexOf(lista.find { it.uuid == uuid })
        repository.deleteItem(lista.find { it.uuid == uuid }!!)
        mAdapter.notifyDataSetChanged()
    }



    private fun abrirCategoria(uuid: UUID) {

        val bundle = Bundle().apply {
            putString("uuid", uuid.toString())
        }
        val navController = findNavController()

        val actionId = R.id.action_categoriesFragment_to_todosFragment

        navController.navigate(actionId, bundle)
    }


}