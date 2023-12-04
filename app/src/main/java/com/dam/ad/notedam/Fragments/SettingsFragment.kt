package com.dam.ad.notedam.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.dam.ad.notedam.Activities.MainActivity
import com.dam.ad.notedam.Config.ConfigFileType
import com.dam.ad.notedam.Config.ConfigStorageType
import com.dam.ad.notedam.Enums.FileType
import com.dam.ad.notedam.Enums.StorageType
import com.dam.ad.notedam.R
import com.dam.ad.notedam.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() , RadioGroup.OnCheckedChangeListener{

    private var _binding:FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private var estadoAnterior: Int? = null;
    private var estadoAnteriorFile : Int? = null
    private var restaurandoEstado = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        initFields()
        createEvents()

        return binding.root
    }

    override fun onResume() {
        estadoAnterior = binding.radioGroupBBDD.checkedRadioButtonId
        estadoAnteriorFile = binding.radioGroupFile.checkedRadioButtonId
        super.onResume()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding.radioGroupBBDD.setOnCheckedChangeListener(null)
        binding.radioGroupFile.setOnCheckedChangeListener(null)
    }

    private fun initFields() {
        when (ConfigStorageType.loadStorageType(activity as MainActivity)) {
            StorageType.Local -> binding.Local.isChecked = true
            StorageType.Sql -> binding.SQL.isChecked = true
            StorageType.MongoDB -> binding.Mongo.isChecked = true
        }
        when (ConfigFileType.loadFileType(activity as MainActivity)) {
            FileType.CSV -> binding.csv.isChecked = true
            FileType.JSON -> binding.JSON.isChecked = true
            FileType.XML -> binding.XML.isChecked = true
        }
    }


    private fun createEvents() {
        binding.radioGroupBBDD.setOnCheckedChangeListener(this)
        binding.radioGroupFile.setOnCheckedChangeListener(this)
    }

    private fun realizarCambioFileType (opcionSeleccionada: Int) {
        Log.v("Preferences", "opcion selecciona : $opcionSeleccionada")
        // Realizar el cambio de configuración
        val fileType = when (opcionSeleccionada) {
            0 -> FileType.CSV
            1 -> FileType.JSON
            2 -> FileType.XML
            else -> FileType.CSV
        }
        ConfigFileType.saveStorageFile(activity as MainActivity, fileType)
    }
    private fun realizarCambioStorageType(opcionSeleccionada: Int) {
        Log.v("Preferences", "opcion selecciona : $opcionSeleccionada")
        // Realizar el cambio de configuración
        val storageType = when (opcionSeleccionada) {
            0 -> StorageType.Local
            1 -> StorageType.Sql
            2 -> StorageType.MongoDB
            else -> StorageType.Local
        }
        ConfigStorageType.saveStorageType(activity as MainActivity, storageType)
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        if (!restaurandoEstado ) {
            AlertDialog.Builder(requireContext())
                .setMessage(R.string.changeConfigText)
                .setPositiveButton("Sí") { dialog, _ ->
                    val opcionSeleccionada : Int = if (group!!.id == binding.radioGroupBBDD.id) {
                        when (checkedId) {
                            binding.Local.id -> 0
                            binding.SQL.id -> 1
                            binding.Mongo.id -> 2
                            else -> -1  // Si ninguno está seleccionado o hay un identificador desconocido
                        }
                    }else {
                        when (checkedId) {
                            binding.csv.id -> 0
                            binding.JSON.id -> 1
                            binding.XML.id -> 2
                            else -> -1
                        }
                    }
                    restaurandoEstado = false
                    estadoAnterior = binding.radioGroupBBDD.checkedRadioButtonId
                    estadoAnteriorFile = binding.radioGroupFile.checkedRadioButtonId
                    dialog.dismiss()
                    if (group.id == binding.radioGroupBBDD.id) {
                        realizarCambioStorageType(opcionSeleccionada)
                    }else {
                        realizarCambioFileType(opcionSeleccionada)
                    }
                }
                .setNegativeButton("No") { dialog, _ ->
                    restaurandoEstado = true
                    binding.radioGroupBBDD.check(estadoAnterior!!)
                    estadoAnterior = binding.radioGroupBBDD.checkedRadioButtonId
                    estadoAnteriorFile = binding.radioGroupFile.checkedRadioButtonId
                    restaurandoEstado = false
                    dialog.dismiss()
                }
                .show()
        }
    }


}