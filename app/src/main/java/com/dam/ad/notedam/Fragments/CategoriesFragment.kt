package com.dam.ad.notedam.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dam.ad.notedam.Activities.MainActivity
import com.dam.ad.notedam.Config.ConfigFileType
import com.dam.ad.notedam.Config.ConfigStorageType
import com.dam.ad.notedam.databinding.FragmentCategoriesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    private var _binding:FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoriesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(activity, ConfigFileType.loadFileType(activity as MainActivity).toString(), Toast.LENGTH_LONG).show()
    }
}