package com.dam.ad.notedam.presentation.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.dam.ad.notedam.databinding.NotaBinding
import com.dam.ad.notedam.presentation.models.NotaToDo


@SuppressLint("NotifyDataSetChanged")
class ProductoAdapter(
    var productList: MutableList<NotaToDo>,

) : RecyclerView.Adapter<ProductoAdapter.ProductViewHolder>(), PopupMenu.OnMenuItemClickListener {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: NotaBinding = NotaBinding.bind(view)


        @SuppressLint("SetTextI18n")
        fun bind(nota: NotaToDo) {

            binding.todoField.text = nota.whatToDo
            binding.selectedCheck
        }

        fun setListener(producto: Producto) {
            val isFavorited = producto.isFavorito

            if (isFavorited) {
                binding.favouriteButton.setImageResource(R.drawable.full_star)
            } else {
                binding.favouriteButton.setImageResource(R.drawable.empty_star)
            }

            /*
            binding.favouriteButton.setOnClickListener {
                // Invertir el estado de favorito y cambiar la imagen
                producto.isFavorito = !isFavorited
                // Llamar a la función de devolución de llamada para notificar el cambio
                if (isFavorited) {
                    binding.favouriteButton.setImageResource(R.drawable.full_star)
                } else if(!isFavorited) {
                    binding.favouriteButton.setImageResource(R.drawable.empty_star)
                }
                listener.onFavourite(producto)
            }
            */


        }

        fun setPopUpMenu(producto: Producto) {
            binding.root.setOnLongClickListener {
                showPopup(binding.root, producto)
                true
            }
        }

    }

    @SuppressLint("ResourceType")
    fun showPopup(view: View, producto: Producto) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.setOnMenuItemClickListener(this)
        popupMenu.inflate(R.menu.popup_menu)
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when(item.itemId) {
                R.id.menu_delete -> {
                    listener.onDeleteProduct(producto)
                    true
                }
                else -> {false}
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.producto, parent, false
        )
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val producto = productList[position]

        holder.bind(producto)
        holder.setListener(producto)
        holder.setPopUpMenu(producto)
    }


    fun setList(listaProductos: MutableList<Producto>) {
        this.productList = listaProductos
        notifyDataSetChanged()
    }

    fun addProducto(producto: Producto) {
        productList.add(producto)
        notifyItemChanged(itemCount - 1)
    }

    fun deleteProducto(id: Int) {
        productList.removeAll { producto -> producto.id == id }
        notifyDataSetChanged()
    }
    //Actualizo el producto en la base de datos.
    fun switchFavorite(producto: Producto) {
        producto.isFavorito = true
        deleteProducto(producto.id)
        addProducto(producto)
    }

    fun getProductsList(): MutableList<Producto> {
        return productList
    }


}

