package com.example.myapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.beans.PokemonBean
import com.example.myapp.databinding.ViewholderPokemonBinding
import com.example.myapp.model.Result
import com.example.myapp.viewholder.PokemonViewHolder

class PokemonListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var pokemonList = arrayListOf<PokemonBean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ViewholderPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        if (holder is PokemonViewHolder) {
            holder.bindView(pokemon)
        }
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    fun processData(data: List<Result>, offset: Int = 0) {
        if (data.isNullOrEmpty()) return

        val list = ArrayList<PokemonBean>()
        for ((index, value) in data.withIndex()) {
            list.add(
                PokemonBean(
                    value.name.replaceFirstChar { it.uppercaseChar() },
                    getPictureUrl(index + 1 + offset),
                    index + offset
                )
            )
        }

        update(list)
    }

    private fun getPictureUrl(number: Int): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$number.png"
    }

    private fun update(data: List<PokemonBean>?) {
        if (data === null) return

        // pokemonList.clear()
        pokemonList.addAll(data)
        notifyDataSetChanged()
    }
}