package com.dam.ad.notedam.presentation.categories

import android.app.AlertDialog
import android.app.Dialog
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import android.os.Bundle
import android.widget.Button
import com.dam.ad.notedam.R

class CreateCategoryFragment : DialogFragment() {

    private lateinit var editText: EditText
    private lateinit var btnConfirm: Button
    private lateinit var btnCancel: Button
    private var listener: OnInputListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_category, null)
        editText = view.findViewById(R.id.etCategory)
        btnConfirm = view.findViewById(R.id.btnConfirm)
        btnCancel = view.findViewById(R.id.btnCancel)

        btnConfirm.setOnClickListener {
            onConfirmClicked()
        }
        btnCancel.setOnClickListener {
            dismiss()
        }

        builder.setView(view)
        return builder.create()
    }

    fun setOnInputListener(listener: OnInputListener) {
        this.listener = listener
    }

    interface OnInputListener {
        fun onInputConfirmed(catName: String)
    }

    private fun onConfirmClicked() {
        val inputText = editText.text.toString()
        if (inputText.contains("|")) {
            AlertDialog.Builder(requireContext()).apply {
                setTitle("Nombre no válido")
                setMessage("El nombre no puede contener el caracter '|'.")
                setNeutralButton("OK") { _, _ -> }
            }
        }
        listener?.onInputConfirmed(inputText)
        dismiss()
    }
}