import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dam.ad.notedam.Adapters.ItemOnClickListener
import com.dam.ad.notedam.Adapters.SubListAdapter
import com.dam.ad.notedam.Models.nota.*
import com.dam.ad.notedam.R
import com.dam.ad.notedam.databinding.ItemNotaImagenBinding
import com.dam.ad.notedam.databinding.ItemNotaListaBinding
import com.dam.ad.notedam.databinding.ItemNotaTextoBinding
import java.util.*

class ItemNotaAdapter(
    private var listItem: MutableList<Nota>,
    private var listener: ItemOnClickListener<Nota>
) : RecyclerView.Adapter<ItemNotaAdapter.ViewHolder>()  {

    // Tipos de vistas
    private val TIPO_TEXTO = 1
    private val TIPO_IMAGEN = 2
    private val TIPO_LISTA = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TIPO_TEXTO -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_nota_texto, parent, false)
                TextoViewHolder(view)
            }
            TIPO_IMAGEN -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_nota_imagen, parent, false)
                ImagenViewHolder(view)
            }
            TIPO_LISTA -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_nota_imagen, parent, false)
                TextoViewHolder(view)
                TextoViewHolder(view)
            }
            else -> throw IllegalArgumentException("Tipo de vista desconocido")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nota = listItem[position]
        holder.bind(nota)
        holder.setListener(nota)
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (listItem[position]) {
            is NotaTexto -> TIPO_TEXTO
            else -> TODO()
        }
    }

    // ViewHolder para notas de texto
    inner class TextoViewHolder(view: View) : ViewHolder(view) {
        val binding = ItemNotaTextoBinding.bind(view)
        override fun bind(nota: Nota) {
            val notaCambiada = nota as NotaTexto
            binding.textoNota.text = notaCambiada.textoNota
            binding.deleteButton.setImageResource(R.drawable.baseline_delete_24)
        }

        override fun setListener(nota: Nota) {
            binding.root.apply {
                setOnClickListener {
                    listener.onClick(nota.uuidNota)
                }
                setOnLongClickListener {
                    listener.onLongClickListener(nota.uuidNota)
                }
            }
        }

    }

    // ViewHolder para notas de imagen
    inner class ImagenViewHolder(view: View) : ViewHolder(view) {
        val binding = ItemNotaImagenBinding.bind(view)
        override fun bind(nota: Nota) {
            val notaImagen = nota as NotaImagen
            notaImagen.uriImagen?.let {
                binding.imagenNota.setImageURI(it)
            }?:let {
                binding.imagenNota.setImageResource(R.drawable.img_default_background)
            }
            binding.textoNotaImagen.text = notaImagen.textoNota
        }

        override fun setListener(nota: Nota) {
            binding.root.apply {
                setOnClickListener {
                    listener.onClick(nota.uuidNota)
                }
                setOnLongClickListener {
                    listener.onLongClickListener(nota.uuidNota)
                }
            }
        }
    }

    inner class ListaViewHolder (view : View) : ViewHolder(view) {
        val binding = ItemNotaListaBinding.bind(view)

        override fun bind(nota: Nota) {
            val notaLista = nota as NotaLista
            binding.textoNotaLista.text = notaLista.textoNota
            binding.recyclerViewLista.adapter = SubListAdapter(
                listItem = notaLista.lista,
                onLongClick = {uuid ->
                    val elemento = notaLista.lista.find { it.uuid == uuid }
                    if (elemento != null) {
                        notaLista.lista.removeIf { it.uuid == elemento.uuid }
                    }
                },
                onClick = { uuid ->
                    val elemento = notaLista.lista.find { it.uuid == uuid }
                    if (elemento != null) {
                        elemento.boolean = !elemento.boolean
                    }
                }
            )
        }

        override fun setListener(nota: Nota) {
            binding.root.apply {
                setOnClickListener {
                    listener.onClick(nota.uuidNota)
                }
                setOnLongClickListener {
                    listener.onLongClickListener(nota.uuidNota)
                }
            }
        }


    }

    // Clase base ViewHolder
    abstract inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(nota: Nota)
        abstract fun setListener(nota : Nota)
    }


}
