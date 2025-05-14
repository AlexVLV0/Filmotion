package com.example.filmotion.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmotion.data.api.RetrofitClient
import com.example.filmotion.databinding.FragmentPeliculasBinding
import com.example.filmotion.model.Pelicula
import com.example.filmotion.ui.main.adapter.PeliculasAdapter
import com.example.filmotion.ui.main.fragments.DetalleFragment
import com.example.filmotion.utils.PreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PeliculasFragment : Fragment() {

    private var _binding: FragmentPeliculasBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PeliculasAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPeliculasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = PreferencesManager(requireContext())
        val userId = prefs.getUserId()

        if (userId == null) {
            Toast.makeText(requireContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        binding.recyclerViewPeliculas.layoutManager = LinearLayoutManager(requireContext())

        adapter = PeliculasAdapter(emptyList()) { pelicula ->
            parentFragmentManager.beginTransaction()
                .replace(
                    com.example.filmotion.R.id.nav_host_fragment,
                    DetalleFragment.newInstance(pelicula)
                )
                .addToBackStack(null)
                .commit()
        }

        binding.recyclerViewPeliculas.adapter = adapter

        val call = RetrofitClient.create(requireContext()).getPeliculasValoradas(userId)

        call.enqueue(object : Callback<List<Pelicula>> {
            override fun onResponse(
                call: Call<List<Pelicula>>,
                response: Response<List<Pelicula>>
            ) {
                if (response.isSuccessful) {
                    val peliculas = response.body() ?: emptyList()
                    adapter.updateList(peliculas)
                } else {
                    Log.e("PeliculasFragment", "Error: ${response.code()}")
                    Toast.makeText(requireContext(), "Error al cargar películas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Pelicula>>, t: Throwable) {
                Log.e("PeliculasFragment", "Fallo de conexión", t)
                Toast.makeText(requireContext(), "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
