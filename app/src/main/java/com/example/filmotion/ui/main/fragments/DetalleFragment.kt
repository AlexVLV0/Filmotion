package com.example.filmotion.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.filmotion.databinding.FragmentDetalleBinding
import com.example.filmotion.model.Pelicula
import com.example.filmotion.ui.main.dialogs.ValoracionDialogFragment

class DetalleFragment : Fragment() {

    private lateinit var binding: FragmentDetalleBinding
    private var pelicula: Pelicula? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pelicula = arguments?.getParcelable("pelicula")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetalleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pelicula?.let {
            binding.textTituloDetalle.text = it.titulo
            binding.textFechaDetalle.text = it.fechaLanzamiento
            binding.textDescripcionDetalle.text = it.descripcion

            Glide.with(this)
                .load(it.imagenUrl)
                .into(binding.imageDetalle)
        }

        binding.btnAgregarResena.setOnClickListener {
            pelicula?.let { peli ->
                ValoracionDialogFragment.newInstance(peli.id)
                    .show(parentFragmentManager, "ValoracionDialog")
            }
        }

    }

    companion object {
        fun newInstance(pelicula: Pelicula): DetalleFragment {
            val fragment = DetalleFragment()
            val args = Bundle()
            args.putParcelable("pelicula", pelicula)
            fragment.arguments = args
            return fragment
        }
    }
}
