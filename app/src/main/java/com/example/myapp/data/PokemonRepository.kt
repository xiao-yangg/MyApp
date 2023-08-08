package com.example.myapp.data

import com.example.myapp.model.Pokedex
import com.example.myapp.model.Pokemon
import com.example.myapp.util.Resource
import io.reactivex.rxjava3.core.Observable
import java.lang.Exception

class PokemonRepository constructor(private val api: IPokemonApi) {

    fun getPokedex(offset: Int, limit: Int): Resource<Observable<Pokedex>> {
        val response = try {
            api.getPokedex(offset, limit)
        } catch (e: Exception) {
            return Resource.Error("Request failed")
        }

        return Resource.Success(response)
    }

    fun getPokemon(name: String): Resource<Observable<Pokemon>> {
        val response = try {
            api.getPokemon(name)
        } catch (e: Exception) {
            return Resource.Error("Request failed")
        }

        return Resource.Success(response)
    }
}