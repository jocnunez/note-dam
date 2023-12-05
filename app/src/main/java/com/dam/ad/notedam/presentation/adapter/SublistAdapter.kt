import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.core.domain.models.notes.Subnota
import com.dam.ad.notedam.R

class SublistAdapter(private val context: Context, private val sublistItems: List<Subnota>) :
    RecyclerView.Adapter<SublistAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.todo_sublist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = sublistItems[position]
        holder.textViewSublist.text = currentItem.fechaCreacion
        holder.checkBoxSublist.isChecked = currentItem.checkBox
    }

    override fun getItemCount(): Int {
        return sublistItems.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewSublist: TextView = itemView.findViewById(R.id.textViewSublist)
        val checkBoxSublist: CheckBox = itemView.findViewById(R.id.checkBoxSublist)
    }
}
