package com.example.filmotion.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.media3.common.util.Log
import com.bumptech.glide.Glide
import com.example.filmotion.databinding.FragmentOlvidadasBinding
import com.example.filmotion.model.Pelicula
import com.example.filmotion.ui.main.dialogs.ValoracionDialogFragment
import com.example.filmotion.utils.PreferencesManager
import com.example.filmotion.data.api.RetrofitClient
import com.example.filmotion.data.model.OlvidadaResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OlvidadasFragment : Fragment() {

    private var _binding: FragmentOlvidadasBinding? = null
    private val binding get() = _binding!!
    private var peliculaActual: Pelicula? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOlvidadasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.contenedorPelicula.visibility = View.INVISIBLE
        binding.btnRevelar.setOnClickListener {
            revelarPelicula()
        }

        binding.contenedorPelicula.setOnClickListener {
            peliculaActual?.let {
                val dialog = ValoracionDialogFragment.newInstance(it.id)
                dialog.show(parentFragmentManager, "DialogValoracion")
                parentFragmentManager.setFragmentResultListener("VALORACION_REALIZADA", this) { _, _ ->
                    binding.contenedorPelicula.visibility = View.INVISIBLE
                    PreferencesManager(requireContext()).clearOlvidadaId()  // 🧹 Elimina la película actual
                    obtenerOlvidada()
                }
            }
        }

        obtenerOlvidada()
    }

    private fun revelarPelicula() {
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 500
        binding.contenedorPelicula.visibility = View.VISIBLE
        binding.contenedorPelicula.startAnimation(fadeIn)

        // 🔒 Desactivar el botón para evitar múltiples revelados
        binding.btnRevelar.isEnabled = false
    }


    private fun obtenerOlvidada() {
        val ctx = context ?: return
        val prefs = PreferencesManager(ctx)
        val userId = prefs.getUserId() ?: return

        val idLocal = prefs.getOlvidadaIdSiValida()
        if (idLocal != null) {
            RetrofitClient.create(ctx).getPeliculasValoradas(userId).enqueue(object : Callback<List<Pelicula>> {
                override fun onResponse(call: Call<List<Pelicula>>, response: Response<List<Pelicula>>) {
                    if (!isAdded || context == null) return

                    val pelicula = response.body()?.find { it.id == idLocal }
                    if (pelicula != null) {
                        peliculaActual = pelicula
                        mostrarPelicula(pelicula)
                    } else {
                        solicitarNuevaOlvidada(userId, prefs)
                    }
                }

                override fun onFailure(call: Call<List<Pelicula>>, t: Throwable) {
                    if (!isAdded || context == null) return
                    Toast.makeText(context, "Error de red", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            solicitarNuevaOlvidada(userId, prefs)
        }
    }


    private fun solicitarNuevaOlvidada(userId: Int, prefs: PreferencesManager) {
        val ctx = context ?: return  // ✅ Previene crashes
        RetrofitClient.create(ctx).getOlvidada(userId).enqueue(object : Callback<OlvidadaResponse> {
            override fun onResponse(call: Call<OlvidadaResponse>, response: Response<OlvidadaResponse>) {
                if (!isAdded || context == null) return

                val pelicula = response.body()?.pelicula
                if (response.isSuccessful && pelicula != null) {
                    peliculaActual = pelicula
                    prefs.saveOlvidada(pelicula.id)
                    mostrarPelicula(pelicula)
                } else {
                    Toast.makeText(context, "No hay películas olvidadas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<OlvidadaResponse>, t: Throwable) {
                if (!isAdded || context == null) return
                Toast.makeText(context, "Fallo de conexión", Toast.LENGTH_SHORT).show()
            }
        })

    }




    private fun mostrarPelicula(pelicula: Pelicula) {
        peliculaActual = pelicula
        binding.textSinOlvidadas.visibility = View.GONE
        binding.textTituloOlvidada.text = pelicula.titulo
        binding.textFechaOlvidada.text = pelicula.fechaLanzamiento
        Glide.with(requireContext()).load(pelicula.imagenUrl).into(binding.imageOlvidada)

        // 👉 No revelamos automáticamente. El usuario debe pulsar el botón.
        binding.contenedorPelicula.visibility = View.INVISIBLE
        // Al final de mostrarPelicula()
        binding.btnRevelar.isEnabled = true
        binding.contenedorPelicula.visibility = View.INVISIBLE

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
