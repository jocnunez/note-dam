package com.dam.ad.notedam.presentation.home

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dam.ad.notedam.R
import com.dam.ad.notedam.controller.categories.ControllerCategories
import com.dam.ad.notedam.databinding.ActivityMainBinding
import com.dam.ad.notedam.models.Format
import com.dam.ad.notedam.models.State
import com.dam.ad.notedam.repositories.categories.RepositoryCategoriesMemory
import com.dam.ad.notedam.services.storage.categories.CategoryStorageCsv
import com.dam.ad.notedam.services.storage.categories.CategoryStorageJson
import com.dam.ad.notedam.services.storage.categories.CategoryStorageService
import com.dam.ad.notedam.services.storage.categories.CategoryStorageXml
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private lateinit var state: State
    private val format: Format = Format.XML
    private val filePath = "${System.getProperty("user.dir")}${File.separator}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        state = State(ControllerCategories(RepositoryCategoriesMemory(), setStorage(), "${this.applicationInfo.dataDir}${File.separator}"), format)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavMenu()
    }

    private fun initNavMenu() {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHost.navController
        binding.bottomNavView.setupWithNavController(navController)
    }

    private fun setStorage(): CategoryStorageService {
        return when (format) {
            Format.CSV -> CategoryStorageCsv()
            Format.XML -> CategoryStorageXml()
            Format.JSON -> CategoryStorageJson()
        }
    }

    fun getState(): State {
        return state
    }

    fun getNav(): NavController {
        return navController
    }

}