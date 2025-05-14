package com.example.filmotion.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmotion.R
import com.example.filmotion.data.api.RetrofitClient
import com.example.filmotion.databinding.FragmentBusquedaBinding
import com.example.filmotion.model.Pelicula
import com.example.filmotion.ui.main.adapter.PeliculasAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BusquedaFragment : Fragment() {

    private var query: String? = null
    private lateinit var binding: FragmentBusquedaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        query = arguments?.getString("query")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBusquedaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. LayoutManager obligatorio
        binding.recyclerViewResultados.layoutManager = LinearLayoutManager(requireContext())

        // 2. Llamada a la API
        query?.let { searchQuery ->
            val call = RetrofitClient.create(requireContext()).buscarPeliculas(query = searchQuery)

            call.enqueue(object : Callback<List<Pelicula>> {
                override fun onResponse(
                    call: Call<List<Pelicula>>,
                    response: Response<List<Pelicula>>
                ) {
                    if (response.isSuccessful) {
                        val resultados = response.body() ?: emptyList()
                        Log.d("BusquedaFragment", "Películas encontradas: ${resultados.size}")
                        val adapter = PeliculasAdapter(resultados) { pelicula ->
                            val detalleFragment = DetalleFragment.newInstance(pelicula)
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.nav_host_fragment, detalleFragment)
                                .addToBackStack(null)
                                .commit()
                        }
                        binding.recyclerViewResultados.adapter = adapter
                    } else {
                        Log.e("BusquedaFragment", "Respuesta fallida: ${response.code()}")
                        Toast.makeText(requireContext(), "Error al obtener resultados", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Pelicula>>, t: Throwable) {
                    Log.e("BusquedaFragment", "Error en la conexión: ${t.message}", t)
                    Toast.makeText(requireContext(), "Error en la conexión", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    companion object {
        fun newInstance(query: String): BusquedaFragment {
            val fragment = BusquedaFragment()
            val args = Bundle()
            args.putString("query", query)
            fragment.arguments = args
            return fragment
        }
    }
}
