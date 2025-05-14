package com.example.filmotion.ui.main.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.filmotion.data.api.RetrofitClient
import com.example.filmotion.data.model.ValoracionResponse
import com.example.filmotion.databinding.DialogValoracionBinding
import com.example.filmotion.utils.PreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ValoracionDialogFragment : DialogFragment() {

    private lateinit var binding: DialogValoracionBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogValoracionBinding.inflate(LayoutInflater.from(context))
        val idPelicula = arguments?.getInt(ARG_PELICULA_ID) ?: -1
        val prefs = PreferencesManager(requireContext())
        val idUsuario = prefs.getUserId()

        if (idUsuario != null) {
            RetrofitClient.create(requireContext())
                .consultarValoracion(idUsuario, idPelicula)
                .enqueue(object : Callback<ValoracionResponse> {
                    override fun onResponse(call: Call<ValoracionResponse>, response: Response<ValoracionResponse>) {
                        if (response.isSuccessful && response.body()?.status == "success") {
                            val valoracion = response.body()?.valoracion
                            binding.ratingBar.rating = valoracion?.puntuacion?.toFloat() ?: 0f
                        }
                    }

                    override fun onFailure(call: Call<ValoracionResponse>, t: Throwable) {}
                })
        }

        binding.btnFeliz.setOnClickListener {
            enviarValoracion(idPelicula, emocion = 1)
        }

        binding.btnTriste.setOnClickListener {
            enviarValoracion(idPelicula, emocion = 0)
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Valorar Película")
            .setView(binding.root)
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .create()
    }

    private fun enviarValoracion(idPelicula: Int, emocion: Int) {
        val prefs = PreferencesManager(requireContext())
        val idUsuario = prefs.getUserId()
        val puntuacion = binding.ratingBar.rating.toInt()

        if (idUsuario == null || puntuacion == 0) {
            Toast.makeText(requireContext(), "Completa la valoración", Toast.LENGTH_SHORT).show()
            return
        }

        val call = RetrofitClient.create(requireContext()).guardarValoracion(
            idUsuario, idPelicula, puntuacion, emocion, "guardarValoracion"
        )

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Valoración guardada", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.setFragmentResult("VALORACION_REALIZADA", Bundle())
                    dismiss()
                }
                else {
                    Toast.makeText(requireContext(), "Error al guardar", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Fallo de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        private const val ARG_PELICULA_ID = "pelicula_id"

        fun newInstance(idPelicula: Int): ValoracionDialogFragment {
            val fragment = ValoracionDialogFragment()
            fragment.arguments = Bundle().apply {
                putInt(ARG_PELICULA_ID, idPelicula)
            }
            return fragment
        }
    }
}
