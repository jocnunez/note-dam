package com.dam.ad.notedam.presentation.settings

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.dam.ad.notedam.databinding.FragmentSettingsBinding
import com.dam.ad.notedam.models.Format
import com.dam.ad.notedam.models.State
import com.dam.ad.notedam.presentation.home.MainActivity
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.AndroidEntryPoint

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding:FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var state: State

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        state = (requireActivity() as MainActivity).getState() //Cogemos la instancia del estado en el main
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.csvExport.isVisible = state.format == Format.CSV
        binding.csvImport.isVisible = state.format == Format.CSV
        binding.xmlExport.isVisible = state.format == Format.XML
        binding.xmlImport.isVisible = state.format == Format.XML
        binding.jsonExport.isVisible = state.format == Format.JSON
        binding.jsonImport.isVisible = state.format == Format.JSON

        binding.csvExport.setOnClickListener {
            state.categoryController.exportAll().onSuccess {
                Toast.makeText(requireContext(), "CSV exportado", Toast.LENGTH_SHORT).show()
            }.onFailure {
                Toast.makeText(requireContext(), "Error al exportar", Toast.LENGTH_SHORT).show()
            }
        }

        binding.csvImport.setOnClickListener {
            state.categoryController.loadAll().onSuccess {
                Toast.makeText(requireContext(), "CSV importado", Toast.LENGTH_SHORT).show()
            }.onFailure {
                Toast.makeText(requireContext(), "Error al importar", Toast.LENGTH_SHORT).show()
            }
        }

        binding.xmlExport.setOnClickListener {
            state.categoryController.exportAll().onSuccess {
                Toast.makeText(requireContext(), "XML exportado", Toast.LENGTH_SHORT).show()
            }.onFailure {
                Toast.makeText(requireContext(), "Error al exportar", Toast.LENGTH_SHORT).show()
            }
        }

        binding.xmlImport.setOnClickListener {
            state.categoryController.loadAll().onSuccess {
                Toast.makeText(requireContext(), "XML importado", Toast.LENGTH_SHORT).show()
            }.onFailure {
                Toast.makeText(requireContext(), "Error al importar", Toast.LENGTH_SHORT).show()
            }
        }

        binding.jsonExport.setOnClickListener {
            state.categoryController.exportAll().onSuccess {
                Toast.makeText(requireContext(), "JSON exportado", Toast.LENGTH_SHORT).show()
            }.onFailure {
                Toast.makeText(requireContext(), "Error al exportar", Toast.LENGTH_SHORT).show()
            }
        }

        binding.jsonImport.setOnClickListener {
            state.categoryController.loadAll().onSuccess {
                Toast.makeText(requireContext(), "JSON importado", Toast.LENGTH_SHORT).show()
            }.onFailure {
                Toast.makeText(requireContext(), "Error al importar", Toast.LENGTH_SHORT).show()
            }
        }
    }

}