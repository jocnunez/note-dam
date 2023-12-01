package com.dam.ad.notedam.presentation.home

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dam.ad.notedam.R
import com.dam.ad.notedam.databinding.ActivityMainBinding
import com.dam.ad.notedam.presentation.models.Preferences
import com.dam.ad.notedam.presentation.settings.MainActivityAction
import dagger.hilt.android.AndroidEntryPoint

const val LOCAL = "local"
const val REMOTO = "remoto"
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainActivityAction {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController:NavController
    lateinit var sharedPref: SharedPreferences
    private lateinit var modoConfiguracion: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        setUpConfigurations()
        setContentView(binding.root)
        initNavMenu()
    }

    private fun initNavMenu() {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHost.navController
        binding.bottomNavView.setupWithNavController(navController)
    }

    override fun setUpConfigurations() {

        modoConfiguracion = sharedPref.getString(
            Preferences.APP_CONF_MODE.clave,
            LOCAL // Valor por defecto si no se encuentra el modo
        )!!
        Log.i("fichero_conf", "El modo de configuracion actual es: $modoConfiguracion")
    }

}