package com.dam.ad.notedam.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dam.ad.notedam.Adapters.SubListAdapter
import com.dam.ad.notedam.Models.nota.*
import com.dam.ad.notedam.R
import com.dam.ad.notedam.databinding.NewNotaBinding

class AddNewNoteDialog : DialogFragment() {

    lateinit var binding : NewNotaBinding
    val lista = mutableListOf<SubList>()
    lateinit var mAdapter : SubListAdapter
    lateinit var mLayoutManager : LinearLayoutManager
    private lateinit var someActivityResultLauncher: ActivityResultLauncher<Intent>
    private var imageUriCargada : Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NewNotaBinding.inflate(layoutInflater, container, false)

        setRecyclerView()
        initEvents()

        someActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                // Manejar el resultado aquí
                if (data != null) {
                    val imagenUri: Uri? = data.data
                    cargarImagenConGlide(imagenUri)
                }
            }
        }

        return binding.root
    }


    private fun initEvents() {

        binding.addButtonTextoNota.setOnClickListener {
            saveNotaTexto()
        }

        binding.textoOption.setOnClickListener {
            cambiarContentOption(cont1 = true, cont2 = false, cont3 = false)
        }

        binding.imagenOption.setOnClickListener {
            cambiarContentOption(cont1 = false, cont2 = true, cont3 = false)
        }

        binding.listaOption.setOnClickListener {
            cambiarContentOption(cont1 = false, cont2 = false, cont3 = true)
        }

        binding.addElementListButton.setOnClickListener {
            addNewSubList()
        }

        binding.imageViewNuevaNota.setOnClickListener {
            seleccionarImagenDeGaleria()
        }

        binding.addButtonImagenNota.setOnClickListener {
            guardarNotaImagen();
        }

        binding.addButtonListaNota.setOnClickListener {
            guardarNotaLista()
        }
    }

    private fun seleccionarImagenDeGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        someActivityResultLauncher.launch(intent)
    }

    private fun cambiarContentOption (cont1 : Boolean, cont2: Boolean, cont3: Boolean) {
        binding.contentOption1.isVisible = cont1
        binding.contentOption2.isVisible = cont2
        binding.contentOption3.isVisible = cont3
    }

    private fun addNewSubList() {
        if (binding.textNewNoteLista.text!!.isNotEmpty()) {
            lista.add(
                SubList(
                    boolean = false,
                    texto = binding.textNewNoteLista.text.toString()
                )
            )
            mAdapter.notifyDataSetChanged()
            binding.textNewNoteLista.setText("")
        }

    }

    private fun setRecyclerView() {
        mAdapter = SubListAdapter(
            lista
        )

        mLayoutManager = LinearLayoutManager(requireContext())

        binding.recyclerView.apply {
            layoutManager = mLayoutManager
            adapter = mAdapter
        }

    }

    override fun onResume() {
        super.onResume()

        // Configura el ancho del diálogo al 75% de la pantalla
        val width = (resources.displayMetrics.widthPixels * 0.75).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setTitle("Nueva Nota")
        dialog.window?.setBackgroundDrawableResource(R.color.semiTransparentBlack)
        return dialog
    }

    interface OnNoteAddedListener {
        fun onNoteAdded(nuevaNota: Nota)
    }

    private var listener: OnNoteAddedListener? = null

    fun setListener(listener: OnNoteAddedListener) {
        this.listener = listener
    }

    private fun saveNotaTexto() {
        if (binding.textNewNoteText.text!!.isNotEmpty()) {
            val nota = NotaTexto(
                textoNota = binding.textNewNoteText.text.toString()
            )
            listener?.onNoteAdded(nota)
            dismiss()
        }
    }

    private fun cargarImagenConGlide(imagenUri: Uri?) {
        imageUriCargada = imagenUri
        if (imagenUri != null) {
            Glide.with(this)
                .load(imagenUri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imageViewNuevaNota)
        }
    }

    private fun guardarNotaImagen() {
        val nota = NotaImagen(
            uriImagen = imageUriCargada,
            textoNota = binding.textNewNoteImage.text.toString()
        )
        listener?.onNoteAdded(nota)

        dismiss()
    }

    private fun guardarNotaLista() {
        if (lista.isNotEmpty()) {
            val nota = NotaLista(
                textoNota = binding.textNewNoteList.text.toString(),
                lista = lista
            )
            listener?.onNoteAdded(nota)

            dismiss()
        }
    }
}