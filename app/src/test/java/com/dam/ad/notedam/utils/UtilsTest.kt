package com.dam.ad.notedam.utils

import android.net.Uri
import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.Note
import com.dam.ad.notedam.models.SublistItem
import java.io.File
import java.time.LocalDateTime

class UtilsTest {
    companion object{
        val categories = listOf(
            Category(
                title = "title1",
                description = "description1",
                priority = 1u,
                notes = listOf(
                    Note.Text(text = "text1", check = false, fechaCreate = LocalDateTime.now()),
                    /*Note.Image(
                        image = Uri.parse("https://static-00.iconduck.com/assets.00/google-icon-2048x2048-czn3g8x8.png"),
                        check = false,
                        fechaCreate = LocalDateTime.now()),
                    Note.Audio(
                        audio = File("https://static-00.iconduck.com/assets.00/google-icon-2048x2048-czn3g8x8.png"),
                        check = false,
                        fechaCreate = LocalDateTime.now()
                    ),*/
                    Note.Sublist(
                        sublist = listOf(
                            SublistItem(check = false, valor = "valor1"),
                            SublistItem(check = false, valor = "valor2"),
                            SublistItem(check = false, valor = "valor3"),
                            SublistItem(check = false, valor = "valor4"),
                            SublistItem(check = false, valor = "valor5"),
                        ),
                        check = false,
                        fechaCreate = LocalDateTime.now()
                    )
                )
            ),
            Category(
                title = "title2",
                description = "description2",
                priority = 2u,
                notes = listOf(
                    Note.Text(text = "text2", check = false, fechaCreate = LocalDateTime.now()),
                )
            )
        )
    }
}