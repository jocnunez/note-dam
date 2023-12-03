package com.dam.ad.notedam.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.dam.ad.notedam.R
import com.dam.ad.notedam.databinding.FragmentSettingsBinding
import com.dam.ad.notedam.presentation.settings.enums.Almacenamiento
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding:FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cargarSpinner()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun cargarSpinner() {
        binding.spinnerAlmacenamiento.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            Almacenamiento.entries.map { it.texto }
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

    }

}
