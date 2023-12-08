package com.dam.ad.notedam.dialogs

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.dam.ad.notedam.Models.nota.NotaTexto
import com.dam.ad.notedam.databinding.NewNotaBinding
import com.dam.ad.notedam.databinding.VerNotaTextoBinding

class VerNotaTextoDialog (val notaTexto: NotaTexto) : DialogFragment() {

    lateinit var binding : VerNotaTextoBinding
    private var onNotaChangedListener: OnNotaChangedListener? = null

    fun setOnNotaChangedListener(listener: OnNotaChangedListener) {
        this.onNotaChangedListener = listener
    }

    override fun onResume() {
        super.onResume()

        // Configura el ancho del diálogo al 75% de la pantalla
        val width = (resources.displayMetrics.widthPixels * 0.75).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = VerNotaTextoBinding.inflate(layoutInflater, container, false)

        initBindings()

        return binding.root
    }

    private fun initBindings() {
        binding.textVerNotaTexto.setText(notaTexto.textoNota)

        binding.guardarNotaTextoButton.setOnClickListener {
            if (binding.textVerNotaTexto.text!!.isNotEmpty()) {
                notaTexto.textoNota = binding.textVerNotaTexto.text.toString()
                onNotaChangedListener?.onNotaChanged(notaTexto.textoNota)
                dismiss()
            }
        }
    }

    interface OnNotaChangedListener {
        fun onNotaChanged(nuevaNota: String)
    }

}