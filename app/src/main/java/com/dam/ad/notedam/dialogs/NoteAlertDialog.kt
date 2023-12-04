package com.dam.ad.notedam.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.dam.ad.notedam.R

class NoteAlertDialog {

    interface NoteAlertListener {
        fun onPositiveButtonClick(text: String)
        fun onNegativeButtonClick()
    }

    companion object {

        fun showTextAlertDialog(context: Context, listener: NoteAlertListener, text: String) {

            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dialogView = inflater.inflate(R.layout.text_alert_dialog, null)

            val title: TextView = dialogView.findViewById(R.id.alertTitle)
            val editText: EditText = dialogView.findViewById(R.id.text)
            val buttonAceptar: Button = dialogView.findViewById(R.id.aceptar)
            val buttonCancelar: Button = dialogView.findViewById(R.id.cancelar)


            if (text != "") {
                title.text = "Editar nota de texto"
                editText.setText(text)
            }

            builder.setView(dialogView)
            val dialog = builder.create()


            buttonCancelar.setOnClickListener {
                listener.onNegativeButtonClick()
                dialog.cancel()
            }

            buttonAceptar.setOnClickListener {
                listener.onPositiveButtonClick(editText.text.toString())
                dialog.cancel()
            }

            dialog.show()
        }

        fun showImageAlertDialog(context: Context, listener: NoteAlertListener, text: String) {
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dialogView = inflater.inflate(R.layout.image_alert_dialog, null)

            val title: TextView = dialogView.findViewById(R.id.alertTitle)
            val editText: EditText = dialogView.findViewById(R.id.url)
            val buttonAceptar: Button = dialogView.findViewById(R.id.aceptar)
            val buttonCancelar: Button = dialogView.findViewById(R.id.cancelar)


            if (text != "") {
                title.text = "Editar nota de imagen"
                editText.setText(text)
            }

            builder.setView(dialogView)
            val dialog = builder.create()


            buttonCancelar.setOnClickListener {
                listener.onNegativeButtonClick()
                dialog.cancel()
            }

            buttonAceptar.setOnClickListener {
                listener.onPositiveButtonClick(editText.text.toString())
                dialog.cancel()
            }

            dialog.show()
        }

        fun showAudioAlertDialog(context: Context, listener: NoteAlertListener, text: String) {
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dialogView = inflater.inflate(R.layout.audio_alert_dialog, null)

            val title: TextView = dialogView.findViewById(R.id.alertTitle)
            val editText: EditText = dialogView.findViewById(R.id.url)
            val buttonAceptar: Button = dialogView.findViewById(R.id.aceptar)
            val buttonCancelar: Button = dialogView.findViewById(R.id.cancelar)


            if (text != "") {
                title.text = "Editar nota de audio"
                editText.setText(text)
            }

            builder.setView(dialogView)
            val dialog = builder.create()


            buttonCancelar.setOnClickListener {
                listener.onNegativeButtonClick()
                dialog.cancel()
            }

            buttonAceptar.setOnClickListener {
                listener.onPositiveButtonClick(editText.text.toString())
                dialog.cancel()
            }

            dialog.show()
        }

        fun showElementAlertDialog(context: Context, listener: NoteAlertListener, text: String) {
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dialogView = inflater.inflate(R.layout.sublist_alert_dialog, null)

            val title: TextView = dialogView.findViewById(R.id.alertTitle)
            val editText: EditText = dialogView.findViewById(R.id.text)
            val buttonAceptar: Button = dialogView.findViewById(R.id.aceptar)
            val buttonCancelar: Button = dialogView.findViewById(R.id.cancelar)


            if (text != "") {
                title.text = "Editar elemento"
                editText.setText(text)
            }

            builder.setView(dialogView)
            val dialog = builder.create()


            buttonCancelar.setOnClickListener {
                listener.onNegativeButtonClick()
                dialog.cancel()
            }

            buttonAceptar.setOnClickListener {
                listener.onPositiveButtonClick(editText.text.toString())
                dialog.cancel()
            }

            dialog.show()
        }

    }
}
