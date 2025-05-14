// RecomendacionesAdapter.kt
package com.example.filmotion.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmotion.databinding.ItemPeliculaBinding
import com.example.filmotion.model.Pelicula

class RecomendacionesAdapter(
    private var peliculas: List<Pelicula>,
    private val onItemClick: (Pelicula) -> Unit
) : RecyclerView.Adapter<RecomendacionesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemPeliculaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pelicula: Pelicula) {
            binding.textTitulo.text = pelicula.titulo
            binding.textFecha.text = pelicula.fechaLanzamiento
            Glide.with(binding.imagePortada.context)
                .load(pelicula.imagenUrl)
                .into(binding.imagePortada)

            binding.root.setOnClickListener {
                onItemClick(pelicula)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPeliculaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(peliculas[position])
    }

    override fun getItemCount(): Int = peliculas.size

    fun updateList(nuevaLista: List<Pelicula>) {
        peliculas = nuevaLista
        notifyDataSetChanged()
    }
}
