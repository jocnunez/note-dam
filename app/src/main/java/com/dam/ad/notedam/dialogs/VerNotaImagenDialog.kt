package com.dam.ad.notedam.dialogs

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dam.ad.notedam.Models.nota.NotaImagen
import com.dam.ad.notedam.R
import com.dam.ad.notedam.databinding.VerNotaImagenBinding

class VerNotaImagenDialog (val notaImagen : NotaImagen) : DialogFragment() {

    lateinit var binding: VerNotaImagenBinding
    private var onNotaChangedListener: OnNotaChangedListener? = null
    private lateinit var someActivityResultLauncher: ActivityResultLauncher<Intent>
    var uriImagen : Uri? = null

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
        binding = VerNotaImagenBinding.inflate(layoutInflater, container, false)

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

        initBindings()

        return binding.root
    }

    private fun initBindings() {
        cargarImagenDesdeUri(notaImagen.uriImagen, binding.verImagenNotaImagen)

        binding.verTextoNotaImagen.setText(notaImagen.textoNota)
        uriImagen = notaImagen.uriImagen

        binding.buttonSaveImagenNota.setOnClickListener {
            notaImagen.textoNota = binding.verTextoNotaImagen.text.toString()
            notaImagen.uriImagen = uriImagen
            onNotaChangedListener?.onNotaChanged(notaImagen)
            dismiss()
        }

        binding.verImagenNotaImagen.setOnClickListener {
            seleccionarImagenDeGaleria()
        }
    }

    private fun cargarImagenDesdeUri(uri: Uri?, imageView: ImageView) {
        uri?.let {
            Glide.with(imageView.context)
                .load(it)
                .into(imageView)
        } ?: run {
            // Si la URI es nula, establece una imagen predeterminada
            imageView.setImageResource(R.mipmap.img_default_layout)
        }
    }

    interface OnNotaChangedListener {
        fun onNotaChanged(nuevaNota: NotaImagen)
    }

    private fun cargarImagenConGlide(imagenUri: Uri?) {
        uriImagen = imagenUri
        if (imagenUri != null) {
            Glide.with(this)
                .load(imagenUri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.verImagenNotaImagen)
        }
    }

    private fun seleccionarImagenDeGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        someActivityResultLauncher.launch(intent)
    }
}
