package com.example.myapp.data

import io.reactivex.rxjava3.core.Observable
import com.example.myapp.model.Pokedex
import com.example.myapp.model.Pokemon
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IPokemonApi {

    @GET("pokemon")
    fun getPokedex(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Observable<Pokedex>

    @GET("pokemon/{name}")
    fun getPokemon(
        @Path("name") name: String
    ): Observable<Pokemon>
}