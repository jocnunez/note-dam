package com.dam.ad.notedam.presentation.todos

import TodoAdapter
import android.R
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.domain.models.Categoria
import com.core.domain.models.notes.*
import com.dam.ad.notedam.databinding.FragmentTodoEditBinding
import com.dam.ad.notedam.databinding.FragmentTodosBinding
import com.dam.ad.notedam.presentation.adapter.OnNoteClickListener
import com.dam.ad.notedam.presentation.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.util.*

@AndroidEntryPoint
class TodosFragment : Fragment(), OnNoteClickListener {

    private var _binding: FragmentTodosBinding? = null
    private val binding get() = _binding!!

    private lateinit var mActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodosBinding.inflate(layoutInflater, container, false)

        mActivity = requireActivity() as MainActivity

        binding.textViewTitleCategory.text =
            mActivity.sharedViewModel.getSharedData()?.nombre ?: "CATEGORÍA NO SELECCIONADA"

        setUpAdapter()

        binding.botonAAdirNota.setOnClickListener {
            if (mActivity.sharedViewModel.getSharedData() != null) {
                createNota(null)
            } else {
                Toast.makeText(mActivity, "No hay ninguna categoría seleccionada", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    /**
     * Cargamos la lista de datos al recycler view, si no hay datos, se crea una lista vacía
     * @author Angel Maroto Chivite
     */
    private fun setUpAdapter() {
        val listTodos = mActivity.sharedViewModel.getSharedData()?.notas ?: mutableListOf()
        binding.recyclerViewTodo.adapter = TodoAdapter(listTodos, this)
        binding.recyclerViewTodo.layoutManager = LinearLayoutManager(requireContext())
    }


    /**
     * Función que abre una ventana modal tanto para crear como para editar una nota, si es nulo crea una nueva nota
     * @author Kevin David Matute Obando
     * @param notaEdited la nota que se va a crear o editar
     */
    private fun createNota(notaEdited: Nota?) {
        val builder = AlertDialog.Builder(requireContext())
        val bindingTodoEdit = FragmentTodoEditBinding.inflate(mActivity.layoutInflater)
        val spinnerTipoNota: Spinner = bindingTodoEdit.spinnerTipo
        val tiposNota = arrayOf("Texto", "Imagen", "Audio", "Sublista")

        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, tiposNota)
        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinnerTipoNota.adapter = spinnerAdapter

        spinnerTipoNota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    //texto
                    0 -> {
                        bindingTodoEdit.imageNota.visibility = View.GONE
                        bindingTodoEdit.editTextNota.visibility = View.VISIBLE
                        bindingTodoEdit.buttonAAdirAudio.visibility = View.GONE
                        bindingTodoEdit.recyclerViewSublist.visibility = View.GONE
                        bindingTodoEdit.datePickerSubnote.visibility = View.GONE
                        bindingTodoEdit.buttonAddSublist.visibility = View.GONE
                    }
                    //imagen
                    1 -> {
                        bindingTodoEdit.imageNota.visibility = View.VISIBLE
                        bindingTodoEdit.editTextNota.visibility = View.GONE
                        bindingTodoEdit.buttonAAdirAudio.visibility = View.GONE
                        bindingTodoEdit.recyclerViewSublist.visibility = View.GONE
                        bindingTodoEdit.datePickerSubnote.visibility = View.GONE
                        bindingTodoEdit.buttonAddSublist.visibility = View.GONE
                    }
                    //audio
                    2 -> {
                        bindingTodoEdit.imageNota.visibility = View.GONE
                        bindingTodoEdit.editTextNota.visibility = View.GONE
                        bindingTodoEdit.buttonAAdirAudio.visibility = View.VISIBLE
                        bindingTodoEdit.recyclerViewSublist.visibility = View.GONE
                        bindingTodoEdit.datePickerSubnote.visibility = View.GONE
                        bindingTodoEdit.buttonAddSublist.visibility = View.GONE
                    }
                    //sublista
                    3 -> {
                        bindingTodoEdit.imageNota.visibility = View.GONE
                        bindingTodoEdit.editTextNota.visibility = View.GONE
                        bindingTodoEdit.buttonAAdirAudio.visibility = View.GONE
                        bindingTodoEdit.recyclerViewSublist.visibility = View.VISIBLE
                        bindingTodoEdit.datePickerSubnote.visibility = View.VISIBLE
                        bindingTodoEdit.buttonAddSublist.visibility = View.VISIBLE

                        /* TODO: hay que que hacer una nota temporal al empezar no ponerla en null desde el principio,
                         si no nunca podremos añadir subnotas*/
                        /* bindingTodoEdit.buttonAddSublist.setOnClickListener {
                             val noteWithSubNote = notaEdited as NotaConSubnota
                             createSubNote(bindingTodoEdit, noteWithSubNote)
                         }*/
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(mActivity, "nada seleccionado", Toast.LENGTH_SHORT).show()
            }
        }


        if (notaEdited != null) {
            // Mostramos los datos de la nota que queremos editar
            bindingTodoEdit.textViewUUID.text = notaEdited.uuid.toString()
            bindingTodoEdit.textViewFecha.text = notaEdited.fechaCreacion
            bindingTodoEdit.checkBoxNota.isChecked = notaEdited.checkBox
            when (notaEdited) {
                is NotaText -> {
                    spinnerTipoNota.setSelection(0)
                    bindingTodoEdit.editTextNota.setText(notaEdited.texto)
                }

                is NotaImagen -> {
                    spinnerTipoNota.setSelection(1)
                    //TODO: implementar la imagen de la nota a editar
                }

                is NotaAudio -> {
                    spinnerTipoNota.setSelection(2)
                    //TODO: implementar el audio de la nota a editar
                }

                is NotaConSubnota -> {
                    spinnerTipoNota.setSelection(3)
                    setUpAdapterSubNote(bindingTodoEdit, notaEdited)
                }
            }
        } else {
            // Generamos datos para una nueva nota
            bindingTodoEdit.textViewUUID.text = UUID.randomUUID().toString()
            bindingTodoEdit.textViewFecha.text = LocalDateTime.now().toString()
            bindingTodoEdit.checkBoxNota.isChecked = false
        }

        builder.setView(bindingTodoEdit.root)
            .setTitle("Introduce los datos de la Nota")
            .setPositiveButton("Aceptar") { _, _ ->
                try {
                    addNote(bindingTodoEdit, spinnerTipoNota, notaEdited)
                } catch (e: Exception) {
                    Toast.makeText(mActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar") { _, _ ->
                Toast.makeText(mActivity, "Se ha cancelado la operación", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    /**
     * Actualizar el recyclerView de las subnotas
     * @author Angel Maroto Chivite
     * @param bindingTodoEdit el binding del fragmento utilizado para editar una nota
     * @param notaEdited la nota que se va a editar las subnotas
     */
    private fun setUpAdapterSubNote(bindingTodoEdit: FragmentTodoEditBinding, notaEdited: NotaConSubnota) {
        bindingTodoEdit.recyclerViewSublist.adapter = TodoAdapter(notaEdited.subnotaList, this)
        bindingTodoEdit.recyclerViewSublist.layoutManager = LinearLayoutManager(requireContext())
    }

    /**
     * Añade una subnota a la nota creada/editada
     * @author Angel Maroto Chivite
     * @param bindingTodoEdit el binding del fragmento utilizado para editar una nota
     * @param notaEdited la nota que se va a editar las subnotas
     */
    private fun createSubNote(bindingTodoEdit: FragmentTodoEditBinding, notaEdited: NotaConSubnota) {
        val fecha: String = bindingTodoEdit.datePickerSubnote.toString()
        val newSubNote = Subnota(UUID.randomUUID(), false, fecha)
        notaEdited.subnotaList.add(newSubNote)
        setUpAdapterSubNote(bindingTodoEdit, notaEdited)
    }

    /**
     * Funcion que añade la nota creada/editada a la lista
     * @author Kevin David Matute Obando
     * @param bindingTodoEdit el binding del fragmento utilizado para editar una nota
     * @param tipoElegido el tipo de la nota que se va a añadir
     * @param uuid el uuid de la nota
     * @param nota la nota que se va a añadir
     */
    private fun addNote(bindingTodoEdit: FragmentTodoEditBinding, tipoElegido: Spinner, nota: Nota?) {
        val fecha: String = bindingTodoEdit.textViewFecha.text.toString()
        val uuid = UUID.fromString(bindingTodoEdit.textViewUUID.text.toString())
        val checkBox = bindingTodoEdit.checkBoxNota.isChecked
        val imagen = bindingTodoEdit.imageNota.toString()
        val texto = bindingTodoEdit.editTextNota.text.toString()
        val subNota: MutableList<Subnota> = mutableListOf()

        val categorySelected: Categoria? = mActivity.sharedViewModel.getSharedData()
        val notaNueva = when (tipoElegido.selectedItem) {
            "Texto" -> NotaText(uuid, checkBox, fecha, texto)
            "Imagen" -> NotaImagen(uuid, checkBox, fecha, imagen)
            "Audio" -> NotaAudio(uuid, checkBox, fecha, "")
            "Sublista" -> NotaConSubnota(uuid, checkBox, fecha, subNota)
            else -> throw IllegalArgumentException("El tipo de la nota no es válido")
        }

        if (nota != null) {
            Toast.makeText(mActivity, "Se editó la nota con uuid: ${notaNueva.uuid}", Toast.LENGTH_SHORT).show()
            editCompleteNote(notaNueva)
        } else {
            Toast.makeText(mActivity, "Se ha creado nueva nota con uuid: ${notaNueva.uuid}", Toast.LENGTH_SHORT).show()
            categorySelected?.notas?.add(notaNueva)
        }
        setUpAdapter()
        mActivity.exportBySourceData()
    }

    override fun deleteNote(nota: Nota) {
        val categorySelected = mActivity.sharedViewModel.getSharedData()

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirmar")
        builder.setMessage("Estas seguro de que quieres borrar?")
        builder.setPositiveButton("Aceptar") { _, _ ->
            Toast.makeText(mActivity, "Tamaño: ${categorySelected?.notas?.size}", Toast.LENGTH_SHORT)
                .show()
            categorySelected?.notas?.remove(nota)
            Toast.makeText(mActivity, "Tamaño: ${categorySelected?.notas?.size}", Toast.LENGTH_SHORT)
                .show()
            setUpAdapter()
            mActivity.exportBySourceData()
        }
        builder.setNegativeButton("Cancelar") { _, _ ->
            Toast.makeText(mActivity, "Se ha cancelado la operación", Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }

    override fun editNote(note: Nota) {
        createNota(note)
    }

    /**
     * Actualiza solamente la notas de una categoría
     * @author Angel Maroto Chivite
     * @param noteEdited la nota que se ha editado
     */
    override fun editCompleteNote(noteEdited: Nota) {
        val categorySelected = mActivity.sharedViewModel.getSharedData()

        if (categorySelected != null) {
            val notesFromCategory = categorySelected.notas

            // El item no cambia de valor, solo cambia el de noteEdited
            val updatedNotes = notesFromCategory.map { itemListening ->
                if (itemListening.uuid == noteEdited.uuid) {
                    when (noteEdited) {
                        is NotaText -> {
                            NotaText(
                                itemListening.uuid,
                                noteEdited.checkBox,
                                itemListening.fechaCreacion,
                                noteEdited.texto
                            )
                        }

                        is NotaImagen -> {
                            NotaImagen(
                                itemListening.uuid,
                                noteEdited.checkBox,
                                itemListening.fechaCreacion,
                                noteEdited.urlImage
                            )
                        }

                        is NotaAudio -> {
                            NotaAudio(
                                itemListening.uuid,
                                noteEdited.checkBox,
                                itemListening.fechaCreacion,
                                noteEdited.audio
                            )
                        }

                        is NotaConSubnota -> {
                            NotaConSubnota(
                                itemListening.uuid,
                                noteEdited.checkBox,
                                itemListening.fechaCreacion,
                                noteEdited.subnotaList.toMutableList()
                            )
                        }

                        else -> throw IllegalArgumentException("El tipo de la nota no es válido")
                    }
                } else {
                    itemListening
                }
            }

            val updatedCategory = categorySelected.copy(notas = updatedNotes.toMutableList())
            mActivity.sharedViewModel.editNotesFromCategory(updatedCategory)
        }
    }
}
