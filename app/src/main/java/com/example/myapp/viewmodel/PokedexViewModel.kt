package com.example.myapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapp.data.IPokemonApi
import com.example.myapp.data.PokemonRepository
import com.example.myapp.data.RetrofitClient
import com.example.myapp.state.PokedexState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PokedexViewModel: ViewModel() {

    private var compositeDisposable = CompositeDisposable()
    private var iPokemonApi: IPokemonApi

    private val _state: MutableStateFlow<PokedexState> = MutableStateFlow(PokedexState.Init)
    val state: StateFlow<PokedexState> = _state.asStateFlow()

    init {
        val retrofit = RetrofitClient.instance
        iPokemonApi = retrofit.create(IPokemonApi::class.java)
    }

    fun fetchData(offset: Int, limit: Int) {
        _state.value = PokedexState.Init

        compositeDisposable.add(
            PokemonRepository(iPokemonApi).getPokedex(offset, limit).data!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ // onSuccess
                    response -> _state.value = PokedexState.Success(response.results)
                }, { // onError
                    _state.value = PokedexState.Failure(it)
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}