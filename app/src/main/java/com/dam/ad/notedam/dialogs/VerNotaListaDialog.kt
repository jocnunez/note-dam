package com.dam.ad.notedam.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dam.ad.notedam.Adapters.SubListAdapter
import com.dam.ad.notedam.Models.nota.NotaLista
import com.dam.ad.notedam.Models.nota.SubList
import com.dam.ad.notedam.databinding.VertNotaListaBinding
import com.dam.ad.notedam.utils.MainContext

class VerNotaListaDialog (val notaLista : NotaLista) : DialogFragment() {

    lateinit var binding : VertNotaListaBinding
    private var onNotaChangedListener: OnNotaChangedListener? = null
    lateinit var mAdapter : SubListAdapter

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
        binding = VertNotaListaBinding.inflate(layoutInflater, container, false)

        setRecyclerView()
        initBindings()

        return binding.root
    }

    private fun setRecyclerView() {
        mAdapter = SubListAdapter(
            listItem = notaLista.lista
        )
        val mLayoutManager = LinearLayoutManager(MainContext.mainActivity)

        binding.recyclerSubListVerNota.apply {
            this.adapter = mAdapter
            this.layoutManager = mLayoutManager
        }

    }

    private fun initBindings() {
        binding.textVerNotaLista.setText(notaLista.textoNota)

        binding.addElementListButtonVerNota.setOnClickListener {
            if (binding.textNewElementVerLista.text!!.isNotEmpty()) {
                notaLista.lista.add(
                    SubList(
                        boolean = false,
                        texto = binding.textNewElementVerLista.text.toString()
                    )
                )
                mAdapter.notifyDataSetChanged()
                binding.textNewElementVerLista.setText("")
            }
        }

        binding.saveButtonVerLista.setOnClickListener {
            notaLista.textoNota = binding.textVerNotaLista.text.toString()
            onNotaChangedListener?.onNotaChanged(notaLista)
            dismiss()
        }
    }

    interface OnNotaChangedListener {
        fun onNotaChanged(notaLista: NotaLista)
    }

}