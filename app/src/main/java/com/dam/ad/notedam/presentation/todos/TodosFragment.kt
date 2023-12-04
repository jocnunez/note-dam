package com.dam.ad.notedam.presentation.todos

import android.app.AlertDialog
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dam.ad.notedam.R
import com.dam.ad.notedam.adapters.ItemNoteAdapter
import com.dam.ad.notedam.adapters.OnElementClickListener
import com.dam.ad.notedam.databinding.FragmentTodosBinding
import com.dam.ad.notedam.dialogs.NoteAlertDialog
import com.dam.ad.notedam.models.Note
import com.dam.ad.notedam.models.State
import com.dam.ad.notedam.presentation.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.time.LocalDateTime
import java.util.UUID

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class TodosFragment : Fragment(), OnElementClickListener<Note<*>> {

    private var _binding: FragmentTodosBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter: ItemNoteAdapter
    private lateinit var mLayoutManager: LinearLayoutManager

    private lateinit var state: State
    private lateinit var nav: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodosBinding.inflate(layoutInflater, container, false)
        setRecyclerView()
        nav = (requireActivity() as MainActivity).getNav()
        nav.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.todosFragment) {
                state.categoryController.getCategorySelected()?.notes?.let { mAdapter.setNotes(it.values.toMutableList()) }
            }
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        state = (requireActivity() as MainActivity).getState() //Cogemos la instancia del estado en el main
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.anadirTodoButton.setOnClickListener {

            if (state.categoryController.getCategorySelected() != null) {
                val builder = AlertDialog.Builder(requireContext())
                    .setItems(
                        arrayOf("Nota de texto", "Nota de imagen", "Nota de audio", "Sublista")
                    ) { dialog, which ->
                        when (which) {
                            0 -> {
                                NoteAlertDialog.showTextAlertDialog(
                                    requireContext(),
                                    object : NoteAlertDialog.NoteAlertListener {
                                        override fun onPositiveButtonClick(text: String) {
                                            val item = Note.Text(
                                                UUID.randomUUID(),
                                                text,
                                                false,
                                                LocalDateTime.now()
                                            )

                                            state.categoryController.addNoteToSelectedCategory(item)
                                            mAdapter.add(item)
                                        }

                                        override fun onNegativeButtonClick() {
                                        }
                                    },
                                    ""
                                )
                            }

                            1 -> {
                                NoteAlertDialog.showImageAlertDialog(
                                    requireContext(),
                                    object : NoteAlertDialog.NoteAlertListener {
                                        override fun onPositiveButtonClick(text: String) {
                                            val item = Note.Image(
                                                UUID.randomUUID(),
                                                text,
                                                false,
                                                LocalDateTime.now()
                                            )

                                            state.categoryController.addNoteToSelectedCategory(item)
                                            mAdapter.add(item)
                                        }

                                        override fun onNegativeButtonClick() {
                                        }
                                    },
                                    ""
                                )
                            }

                            2 -> {
                                NoteAlertDialog.showAudioAlertDialog(
                                    requireContext(),
                                    object : NoteAlertDialog.NoteAlertListener {
                                        override fun onPositiveButtonClick(text: String) {
                                            val item = Note.Audio(
                                                UUID.randomUUID(),
                                                File(text),
                                                false,
                                                LocalDateTime.now()
                                            )

                                            state.categoryController.addNoteToSelectedCategory(item)
                                            mAdapter.add(item)
                                        }

                                        override fun onNegativeButtonClick() {
                                        }
                                    },
                                    ""
                                )
                            }

                            3 -> {
                                val item = Note.Sublist(
                                    UUID.randomUUID(),
                                    mutableMapOf(),
                                    false,
                                    LocalDateTime.now()
                                )

                                state.categoryController.addNoteToSelectedCategory(item)
                                mAdapter.add(item)
                            }
                        }
                    }
                builder.create().show()
            } else {
                Toast.makeText(requireContext(), "No tiene ninguna categoría seleccionada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setRecyclerView() {

        val list = state.categoryController.getCategorySelected()?.notes ?: mutableMapOf()

        mAdapter = ItemNoteAdapter(list.values.toMutableList(), this, state)
        mLayoutManager = LinearLayoutManager(requireContext())

        binding.todoCategory.apply {
            layoutManager = mLayoutManager
            adapter = mAdapter
        }
    }

    private fun addNote(note: Note<*>) {
        state.categoryController.addNoteToSelectedCategory(note)
        mAdapter.add(note)
    }

    override fun onClick(item: Note<*>) {

    }

    override fun onLongClick(item: Note<*>) {

        val builder1 = AlertDialog.Builder(requireContext())
            .setItems(
                arrayOf("Editar", "Eliminar")
            ) { dialog, which ->
                if (which == 0) {
                    when (item) {
                        is Note.Text -> {
                            NoteAlertDialog.showTextAlertDialog(
                                requireContext(),
                                object : NoteAlertDialog.NoteAlertListener {
                                    override fun onPositiveButtonClick(text: String) {
                                        val newItem = item.copy(text = text)

                                        state.categoryController.addNoteToSelectedCategory(newItem)
                                        state.categoryController.getCategorySelected()?.notes
                                            ?.let { notes -> mAdapter.setNotes(notes.values.toMutableList()) }

                                    }

                                    override fun onNegativeButtonClick() {
                                    }
                                },
                                item.text
                            )
                        }
                        is Note.Image -> {
                            NoteAlertDialog.showImageAlertDialog(
                                requireContext(),
                                object : NoteAlertDialog.NoteAlertListener {
                                    override fun onPositiveButtonClick(text: String) {

                                        val newItem = item.copy(image = text)

                                        state.categoryController.addNoteToSelectedCategory(newItem)
                                        state.categoryController.getCategorySelected()?.notes
                                            ?.let { notes -> mAdapter.setNotes(notes.values.toMutableList()) }

                                    }

                                    override fun onNegativeButtonClick() {
                                    }
                                },
                                item.image
                            )
                        }
                        is Note.Audio -> {
                            NoteAlertDialog.showAudioAlertDialog(
                                requireContext(),
                                object : NoteAlertDialog.NoteAlertListener {
                                    override fun onPositiveButtonClick(text: String) {
                                        val newItem = item.copy(audio = File(text))

                                        state.categoryController.addNoteToSelectedCategory(item)
                                        state.categoryController.getCategorySelected()?.notes
                                            ?.let { notes -> mAdapter.setNotes(notes.values.toMutableList()) }

                                    }

                                    override fun onNegativeButtonClick() {
                                    }
                                },
                                item.audio.toString()
                            )
                        }
                        is Note.Sublist -> {

                        }
                    }
                } else {
                    val builder2 = AlertDialog.Builder(requireContext())

                    builder2.setTitle("Alerta")
                        .setMessage("¿Estás seguro de que deseas borrar la nota?")

                    builder2.setPositiveButton("Sí") { _, _ ->
                        state.categoryController.removeNoteFromSelectedCategory(item)
                        mAdapter.delete(item)
                    }

                    builder2.setNegativeButton("No") { _, _ ->

                    }

                    builder2.create().show()
                }
            }
        builder1.create().show()

    }

}