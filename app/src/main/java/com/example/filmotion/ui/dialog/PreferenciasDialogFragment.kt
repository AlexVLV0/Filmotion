package com.example.filmotion.ui.main.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.filmotion.databinding.DialogPreferenciasBinding
import com.example.filmotion.utils.PreferencesManager

class PreferenciasDialogFragment : DialogFragment() {

    private lateinit var binding: DialogPreferenciasBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogPreferenciasBinding.inflate(LayoutInflater.from(context))
        val prefs = PreferencesManager(requireContext())

        // Preseleccionar si hay valores
        binding.radioGroupFeliz.clearCheck()
        when (prefs.getPreferenciaFeliz()) {
            "1" -> binding.radioFelizDeseaFeliz.isChecked = true
            "0" -> binding.radioFelizDeseaTriste.isChecked = true
        }

        binding.radioGroupTriste.clearCheck()
        when (prefs.getPreferenciaTriste()) {
            "1" -> binding.radioTristeDeseaFeliz.isChecked = true
            "0" -> binding.radioTristeDeseaTriste.isChecked = true
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Preferencias emocionales")
            .setView(binding.root)
            .setPositiveButton("Guardar") { _, _ ->
                val felizDesea = when (binding.radioGroupFeliz.checkedRadioButtonId) {
                    binding.radioFelizDeseaFeliz.id -> "1"
                    binding.radioFelizDeseaTriste.id -> "0"
                    else -> null
                }

                val tristeDesea = when (binding.radioGroupTriste.checkedRadioButtonId) {
                    binding.radioTristeDeseaFeliz.id -> "1"
                    binding.radioTristeDeseaTriste.id -> "0"
                    else -> null
                }

                if (felizDesea != null) prefs.savePreferenciaFeliz(felizDesea)
                if (tristeDesea != null) prefs.savePreferenciaTriste(tristeDesea)

                Toast.makeText(context, "Preferencias guardadas", Toast.LENGTH_SHORT).show()
                setFragmentResult("PREFERENCIAS_ACTUALIZADAS", Bundle())
            }
            .setNegativeButton("Cancelar", null)
            .create()
    }
}

