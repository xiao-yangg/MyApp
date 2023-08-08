package com.example.myapp.state

import com.example.myapp.model.Result

sealed class PokedexState {

    object Init: PokedexState()

    data class Success(val data: List<Result>): PokedexState()

    data class Failure(val error: Throwable): PokedexState()
}
