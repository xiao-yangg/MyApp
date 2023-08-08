package com.example.myapp.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapp.beans.PokemonBean
import com.example.myapp.databinding.ViewholderPokemonBinding

class PokemonViewHolder(private val binding: ViewholderPokemonBinding): RecyclerView.ViewHolder(binding.root) {

    fun bindView(pokemon: PokemonBean) {
        binding.pokemonName.text = pokemon.name
        Glide.with(binding.root.context).load(pokemon.imageUrl).into(binding.pokemonImage)
    }
}