package com.example.filmotion.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.filmotion.databinding.DialogPreferenciasBinding
import com.example.filmotion.utils.PreferencesManager

class EmocionDialogFragment : DialogFragment() {

    private lateinit var binding: DialogPreferenciasBinding  // Usa un layout básico si no tienes uno nuevo

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val prefs = PreferencesManager(requireContext())

        return AlertDialog.Builder(requireContext())
            .setTitle("¿Cómo te sientes hoy?")
            .setMessage("Elige cómo te sientes hoy para darte mejores recomendaciones.")
            .setPositiveButton("😊 Feliz") { _, _ ->
                prefs.saveEmocionDiaria("1")
                dismiss()
            }
            .setNegativeButton("😢 Triste") { _, _ ->
                prefs.saveEmocionDiaria("0")
                dismiss()
            }
            .setCancelable(false)
            .create()
    }
}
