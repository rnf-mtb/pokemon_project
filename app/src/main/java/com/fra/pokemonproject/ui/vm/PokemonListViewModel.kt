package com.fra.pokemonproject.ui.vm

import android.util.Log
import androidx.lifecycle.*
import com.fra.pokemonproject.model.Pokemon
import com.fra.pokemonproject.model.PokemonResponse
import com.fra.pokemonproject.model.PokemonResponseWrapper
import com.fra.pokemonproject.repo.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel(), LifecycleObserver {
    private val _allPokemon = MutableLiveData<PokemonResponseWrapper>()
    val allPokemonWrapper : LiveData<PokemonResponseWrapper> = _allPokemon
    private val _pokemon = MutableLiveData<PokemonResponseWrapper>()
    val pokemonWrapper : LiveData<PokemonResponseWrapper> = _pokemon

    init {
        Log.d("PokemonListViewModel", "init called")
        getAllPokemon(0)
    }

    fun getAllPokemon(page: Int) = viewModelScope.launch {
        try {
            Log.d("PokemonListViewModel", "trying getAllPokemon")
            _allPokemon.postValue(PokemonResponseWrapper("OK", pokemonRepository.getAllPaged(page), page))
        } catch (e: Exception) {
            Log.d("PokemonListViewModel", String.format("getAllPokemon KO - exception: %s", e.message))
            _allPokemon.postValue(PokemonResponseWrapper("KO"))
        }
    }

    fun getSinglePokemon(name: String) = viewModelScope.launch {
        try {
            Log.d("PokemonListViewModel", "trying getSinglePokemon")
            _pokemon.postValue(PokemonResponseWrapper("OK", PokemonResponse(0, null, null, listOf(pokemonRepository.getPokemonFromInfo(name) ?: Pokemon()))))
        } catch (e: Exception) {
            Log.d("PokemonListViewModel", String.format("getSinglePokemon KO - exception: %s", e.message))
            _pokemon.postValue(PokemonResponseWrapper("KO"))
        }
    }

}