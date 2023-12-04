package com.dam.ad.notedam.dialogs

import android.app.AlertDialog
import android.content.Context
import android.text.Editable
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.dam.ad.notedam.R
import java.util.UUID

class CategoryAlertDialog {

    interface CategoryAlertListener {
        fun onPositiveButtonClick(text1: String, text2: String, text3: String)
        fun onNegativeButtonClick()
    }

    companion object {

        fun showAlertDialog(context: Context, listener: CategoryAlertListener, text1: String, text2: String, text3: String) {
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dialogView = inflater.inflate(R.layout.category_alert_dialog, null)

            val title: TextView = dialogView.findViewById(R.id.alertTitle)
            val editText1: EditText = dialogView.findViewById(R.id.name)
            val editText2: EditText = dialogView.findViewById(R.id.description)
            val editText3: EditText = dialogView.findViewById(R.id.priority)
            val buttonAceptar: Button = dialogView.findViewById(R.id.aceptar)
            val buttonCancelar: Button = dialogView.findViewById(R.id.cancelar)


            if (text1 != "") {
                title.text = "Editar categoría"
                editText1.setText(text1)
                editText2.setText(text2)
                editText3.setText(text3)
            }

            builder.setView(dialogView)
            val dialog = builder.create()


            buttonCancelar.setOnClickListener {
                listener.onNegativeButtonClick()
                dialog.cancel()
            }

            buttonAceptar.setOnClickListener {
                listener.onPositiveButtonClick(editText1.text.toString(), editText2.text.toString(), editText3.text.toString())
                dialog.cancel()
            }

            dialog.show()
        }
    }
}
