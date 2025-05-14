package com.example.filmotion.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmotion.data.api.RetrofitClient
import com.example.filmotion.databinding.FragmentRandomBinding
import com.example.filmotion.model.Pelicula
import com.example.filmotion.ui.dialog.EmocionDialogFragment
import com.example.filmotion.ui.main.adapter.PeliculasAdapter
import com.example.filmotion.utils.PreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RandomFragment : Fragment() {

    private var _binding: FragmentRandomBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: PreferencesManager
    private lateinit var adapter: PeliculasAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRandomBinding.inflate(inflater, container, false)
        prefs = PreferencesManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerRecomendaciones.layoutManager = LinearLayoutManager(requireContext())
        adapter = PeliculasAdapter(emptyList()) { pelicula ->
            parentFragmentManager.beginTransaction()
                .replace(
                    com.example.filmotion.R.id.nav_host_fragment,
                    DetalleFragment.newInstance(pelicula)
                )
                .addToBackStack(null)
                .commit()
        }
        binding.recyclerRecomendaciones.adapter = adapter

        if (!prefs.isEmocionDelDiaValida()) {
            val dialog = EmocionDialogFragment()
            dialog.show(parentFragmentManager, "EmocionDialog")
        } else {
            obtenerRecomendaciones()
        }

        parentFragmentManager.setFragmentResultListener("EMOCION_SELECCIONADA", this) { _, _ ->
            obtenerRecomendaciones()
        }
    }

    private fun obtenerRecomendaciones() {
        val userId = prefs.getUserId() ?: return
        val emocion = prefs.getEmocionDelDia() ?: return
        val preferencia = when (emocion) {
            "1" -> prefs.getPreferenciaFeliz()
            "0" -> prefs.getPreferenciaTriste()
            else -> null
        } ?: return

        RetrofitClient.create(requireContext())
            .getRecomendaciones(userId, preferencia)
            .enqueue(object : Callback<List<Pelicula>> {
                override fun onResponse(
                    call: Call<List<Pelicula>>,
                    response: Response<List<Pelicula>>
                ) {
                    if (!isAdded || _binding == null) return
                    val lista = response.body() ?: emptyList()
                    adapter.updateList(lista)
                }

                override fun onFailure(call: Call<List<Pelicula>>, t: Throwable) {
                    if (!isAdded || _binding == null) return
                    Toast.makeText(context, "Fallo de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
