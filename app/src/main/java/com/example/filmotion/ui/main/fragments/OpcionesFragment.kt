package com.example.filmotion.ui.main.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.filmotion.databinding.FragmentOpcionesBinding
import com.example.filmotion.ui.login.LoginActivity
import com.example.filmotion.ui.main.dialogs.PreferenciasDialogFragment
import com.example.filmotion.utils.PreferencesManager

class OpcionesFragment : Fragment() {

    private var _binding: FragmentOpcionesBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOpcionesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = PreferencesManager(requireContext())

        // Dark mode
        binding.switchModoOscuro.isChecked =
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

        binding.switchModoOscuro.setOnCheckedChangeListener { _, isChecked ->
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked)
                    AppCompatDelegate.MODE_NIGHT_YES
                else
                    AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        // Preferencias emocionales
        binding.btnPreferencias.setOnClickListener {
            val dialog = PreferenciasDialogFragment()
            dialog.show(parentFragmentManager, "PreferenciasDialog")

            parentFragmentManager.setFragmentResultListener("PREFERENCIAS_ACTUALIZADAS", this) { _, _ ->
                mostrarResumenPreferencias()
            }
        }

        mostrarResumenPreferencias()


        // Cerrar sesión
        binding.btnCerrarSesion.setOnClickListener {
            prefs.clearAll()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.textAcerca.setOnClickListener {
            Toast.makeText(requireContext(), "Filmotion v1.0 - Desarrollado por Ti", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun mostrarResumenPreferencias() {
        val prefs = PreferencesManager(requireContext())
        val felizPref = prefs.getPreferenciaFeliz() // cuando estoy feliz
        val tristePref = prefs.getPreferenciaTriste() // cuando estoy triste

        val texto = StringBuilder()
        texto.append("Cuando estás ")
            .append(if (felizPref != null) "feliz" else "?")
            .append(", quieres ver películas ")
            .append(if (felizPref == "1") "felices" else "tristes")
            .append(".\n")

        texto.append("Cuando estás ")
            .append(if (tristePref != null) "triste" else "?")
            .append(", quieres ver películas ")
            .append(if (tristePref == "1") "felices" else "tristes")
            .append(".")

        binding.textResumenPreferencias.text = texto.toString()
    }

}
