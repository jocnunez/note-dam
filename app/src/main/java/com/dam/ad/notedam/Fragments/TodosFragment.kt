package com.dam.ad.notedam.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.ParcelUuid
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dam.ad.notedam.Activities.MainActivity
import com.dam.ad.notedam.Adapters.ItemCategoriaAdapter
import com.dam.ad.notedam.Adapters.ItemNotaAdapter
import com.dam.ad.notedam.Adapters.ItemOnClickListener
import com.dam.ad.notedam.Config.ConfigFileType
import com.dam.ad.notedam.Config.ConfigStorageType
import com.dam.ad.notedam.Models.Categoria
import com.dam.ad.notedam.Models.ToDOList
import com.dam.ad.notedam.R
import com.dam.ad.notedam.databinding.FragmentTodosBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class TodosFragment : Fragment(), ItemOnClickListener<ToDOList> {

    private var _binding: FragmentTodosBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter : ItemCategoriaAdapter
    private lateinit var mLayoutManager : LinearLayoutManager


    val lista: MutableList<ToDOList> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var uuid: UUID? = arguments?.getString("uuid")?.let { UUID.fromString(it) }

        if (uuid != null) {
            saveLastCategory(uuid)
            Toast.makeText(activity, uuid.toString(), Toast.LENGTH_LONG).show()
        }else {
            val sharedPreferences : SharedPreferences = requireActivity().getSharedPreferences(
                ConfigStorageType.preferencesName,
                Context.MODE_PRIVATE)

            val res = sharedPreferences.getString("lastUUUID", null)
            if (res != null) {
                uuid = UUID.fromString(res)
                Toast.makeText(activity, uuid.toString(), Toast.LENGTH_LONG).show()
            }

        }
        activity?.invalidateOptionsMenu()
        _binding = FragmentTodosBinding.inflate(layoutInflater, container, false)
        return binding.root
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

    override fun onClick(uuid: UUID) {
        TODO("Not yet implemented")
    }

    override fun onLongClickListener(uuid: UUID): Boolean {
        TODO("Not yet implemented")
    }


}