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
import com.dam.ad.notedam.Adapters.ItemCategoriaAdapter
import com.dam.ad.notedam.Adapters.ItemOnClickListener
import com.dam.ad.notedam.Models.Categoria
import com.dam.ad.notedam.R
import com.dam.ad.notedam.databinding.FragmentCategoriesBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class CategoriesFragment() : Fragment() , ItemOnClickListener<Categoria>{

    private var _binding:FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter : ItemCategoriaAdapter
    private lateinit var mLayoutManager : LinearLayoutManager
    val lista: MutableList<Categoria> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentCategoriesBinding.inflate(layoutInflater, container, false)


        setRecyclerView()

        return binding.root
    }



    private fun setRecyclerView() {
        // Cargar Lista



        // Crear el adaptador y configurar el RecyclerView
        mAdapter = ItemCategoriaAdapter(lista, this,
            onArrowUpClick = { position : Int ->
                if (position > 0) {
                    val item = lista.removeAt(position)
                    lista.add(position - 1, item)
                    mAdapter.notifyItemMoved(position, position - 1)
                }
            },
           onArrowDownClick = { position ->
               if (position < lista.size - 1) {
                   val item = lista.removeAt(position)
                   lista.add(position + 1, item)
                   mAdapter.notifyItemMoved(position, position + 1)
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

            mAdapter.notifyDataSetChanged()

            Toast.makeText(requireContext(), "Nombre actualizado", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun onDelete(uuid: UUID) {
        Log.d("CategoriaAdapter", "onDelete: $uuid")
        val posicion = lista.indexOf(lista.find { it.uuid == uuid })
        mAdapter.removeItem(posicion)
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