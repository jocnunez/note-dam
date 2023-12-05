import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.core.domain.models.notes.*
import com.dam.ad.notedam.R
import com.dam.ad.notedam.databinding.TodoBinding
import com.dam.ad.notedam.presentation.adapter.OnNoteClickListener

class TodoAdapter(
    private val todoItems: List<Nota>,
    private val listener: OnNoteClickListener
) : Adapter<TodoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo, parent, false)
        return ViewHolder(view)
    }

    // TODO: el audio y la imagen (manipular los strings)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = todoItems[position]
        setListener(holder, currentItem)

        holder.checkBoxComplete.isChecked = currentItem.checkBox

        when (currentItem) {
            is NotaText -> {
                holder.titleTodo.text = "NOTA TEXTO"
                holder.textViewTodo.text = currentItem.texto
                holder.image.visibility = View.GONE
                holder.buttonAudio.visibility = View.GONE
            }

            is NotaImagen -> {
                holder.titleTodo.text = "NOTA IMAGEN"
                holder.buttonAudio.visibility = View.GONE
                holder.textViewTodo.visibility = View.GONE
                val imageUrl = currentItem.urlImage
                Glide.with(holder.itemView.context)
                    .load(imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background) // Imagen por defecto
                    .error(R.drawable.ic_launcher_background) // Imagen en caso de error
                    .into(holder.image)
            }

            is NotaAudio -> {
                holder.titleTodo.text = "NOTA AUDIO"
                holder.image.visibility = View.GONE
                holder.textViewTodo.visibility = View.GONE
            }

            is NotaConSubnota -> {
                holder.titleTodo.text = "NOTA SUBLISTA"
                holder.image.visibility = View.GONE
                holder.buttonAudio.visibility = View.GONE
                holder.textViewTodo.visibility = View.GONE
                // RecyclerView secundario de la subLista
                val sublistRecyclerView = holder.recyclerViewSublist
                val layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
                sublistRecyclerView.adapter =
                    SublistAdapter(holder.itemView.context, currentItem.subnotaList.toMutableList())
                sublistRecyclerView.layoutManager = layoutManager
            }
        }
    }

    /**
     * Eventos/escuchadores de cada nota
     * @author Angel Maroto Chivite
     * @param holder
     */
    private fun setListener(holder: ViewHolder, item: Nota) {
        holder.itemView.setOnLongClickListener {
            showPopUpMenu(item, holder.itemView)
            true
        }

        // TODO: no está bien configurado junto al checBox de editar del menú contextual
        /*holder.checkBoxComplete.setOnCheckedChangeListener { _, _ ->
            listener.editCompleteNote(item)
        }*/
    }

    /**
     * Función que abre un menu contextual con las opciones de editar y eliminar nota
     * @autor Kevin David Matute Obando
     * @param item la nota que se ha seleccionado
     * @param itemView la vista de la nota
     */
    private fun showPopUpMenu(item: Nota, itemView: View) {
        val binding: TodoBinding = TodoBinding.bind(itemView)
        val popupMenu = PopupMenu(binding.root.context, itemView)
        popupMenu.menuInflater.inflate(R.menu.menu_categorias, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { itemMenu ->
            when (itemMenu.itemId) {
                R.id.botonDesplegableEditar -> {
                    listener.editNote(item)
                    true
                }

                R.id.botonDesplegableEliminar -> {
                    listener.deleteNote(item)
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    override fun getItemCount(): Int {
        return todoItems.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBoxComplete: CheckBox = itemView.findViewById(R.id.checkBoxComplete)
        val titleTodo: TextView = itemView.findViewById(R.id.titleTodo)

        val textViewTodo: TextView = itemView.findViewById(R.id.textViewTodo)
        val image: ImageView = itemView.findViewById(R.id.imageTodo)
        val buttonAudio: Button = itemView.findViewById(R.id.buttonAudioTodo)
        val recyclerViewSublist: RecyclerView = itemView.findViewById(R.id.recyclerViewSublist)
    }
}
