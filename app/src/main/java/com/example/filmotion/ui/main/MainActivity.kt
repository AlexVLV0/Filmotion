package com.example.filmotion.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.filmotion.R
import com.example.filmotion.databinding.ActivityMainBinding
import com.example.filmotion.ui.login.LoginActivity
import com.example.filmotion.ui.main.fragments.BusquedaFragment
import com.example.filmotion.ui.main.fragments.PeliculasFragment
import com.example.filmotion.ui.main.fragments.OpcionesFragment
import com.example.filmotion.ui.main.fragments.RandomFragment
import com.example.filmotion.ui.main.fragments.OlvidadasFragment
import com.example.filmotion.utils.PreferencesManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefs: PreferencesManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    openSearchFragment(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false // Podrías hacer búsqueda en vivo aquí
            }
        })

        prefs = PreferencesManager(this)
        Log.d("MainActivity", "onCreate ejecutado")

        // Verificar si el token existe, si no, redirigir al login
        if (!prefs.hasToken()) {
            Log.d("MainActivity", "Token no encontrado, redirigiendo a Login")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setupBottomNavigation()
        openFragment(PeliculasFragment())
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_peliculas -> openFragment(PeliculasFragment())
                R.id.nav_random -> openFragment(RandomFragment())
                R.id.nav_olvidadas -> openFragment(OlvidadasFragment())
                R.id.nav_perfil -> openFragment(OpcionesFragment())
                else -> false
            }
        }
    }

    private fun openFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
        return true
    }

    private fun openSearchFragment(query: String) {
        val fragment = BusquedaFragment.newInstance(query)
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }
}
