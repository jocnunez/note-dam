package com.dam.ad.notedam.Fragments

import ItemNotaAdapter
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dam.ad.notedam.Adapters.ItemOnClickListener
import com.dam.ad.notedam.Config.ConfigFileType
import com.dam.ad.notedam.Config.ConfigStorageType
import com.dam.ad.notedam.Models.nota.*
import com.dam.ad.notedam.databinding.FragmentTodosBinding
import com.dam.ad.notedam.repositories.INotaRepository
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class TodosFragment : Fragment(), ItemOnClickListener<Nota> {

    private var _binding: FragmentTodosBinding? = null
    private val binding get() = _binding!!


    private lateinit var repository : INotaRepository
    private lateinit var mAdapter : ItemNotaAdapter
    private lateinit var mLayoutManager : LinearLayoutManager

    var uuid: UUID? = null

    private val lista: MutableList<Nota> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTodosBinding.inflate(layoutInflater, container, false)
        loadLastList()
        setRecyclerView()

        binding.addNewButton.setOnClickListener { addNewNota() }
        return binding.root
    }

    private fun setRecyclerView() {
        // Cargar Lista Notas

        lista.add(NotaTexto("Prueba1"))
        lista.add(NotaImagen(null, "Prueba2"))
        lista.add(NotaLista("Hacer" , mutableListOf(
            SubList(boolean = true, texto = "Prueba3"),
            SubList(boolean = false, texto = "Prueba4")
        )))

        mAdapter = ItemNotaAdapter(
            lista,
            this
        )

        mLayoutManager = LinearLayoutManager(requireContext())

        binding.recylerNotas.apply {
            layoutManager = mLayoutManager
            adapter = mAdapter
        }
        enableDragAndDrop(true)
    }

    private fun loadLastList() {
        uuid = arguments?.getString("uuid")?.let { UUID.fromString(it) }

        if (uuid != null) {
            saveLastCategory(uuid!!)
            Toast.makeText(activity, uuid.toString(), Toast.LENGTH_LONG).show()
        } else {
            val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences(
                ConfigStorageType.preferencesName,
                Context.MODE_PRIVATE
            )

            val res = sharedPreferences.getString("lastUUUID", null)
            if (res != null) {
                uuid = UUID.fromString(res)
                Toast.makeText(activity, uuid.toString(), Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun saveLastCategory(uuid: UUID) {
        try {
            val prefs: SharedPreferences = requireActivity().getSharedPreferences(
                ConfigFileType.preferencesName,
                Context.MODE_PRIVATE)
            val edit: SharedPreferences.Editor = prefs.edit()
            edit.putString("lastUUUID", uuid.toString())
            edit.apply()
        } catch (e: Exception) {
            // Manejar cualquier excepción que pueda ocurrir al guardar las preferencias
            e.printStackTrace()
        }
    }

    private fun addNewNota() {
        binding.backgroundOverlay.visibility = View.VISIBLE

        val dialog = AddNewNoteFragment()

        // Configura un listener para recibir datos de la ventana modal
        dialog.setListener(object : AddNewNoteFragment.OnNoteAddedListener {
            override fun onNoteAdded(nuevaNota: Nota) {
                // Agrega la nueva nota a tu lista y actualiza el adaptador
                lista.add(nuevaNota)
                mAdapter.notifyDataSetChanged()

                binding.backgroundOverlay.visibility = View.INVISIBLE
            }
        })

        dialog.show(requireActivity().supportFragmentManager, "CustomDialogFragment")
    }

    override fun onClick(uuid: UUID) {

    }

    override fun onLongClickListener(uuid: UUID): Boolean {
        return true
    }

    private fun enableDragAndDrop(boolean: Boolean) {
        if (!boolean) {
            return
        }
        // Configurar el ItemTouchHelper
        val itemTouchHelperCallback = object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                return makeMovementFlags(dragFlags, 0)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                mAdapter.onItemMove(fromPosition, toPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // No es necesario implementar esto para el arrastrar y soltar
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recylerNotas)
    }


}