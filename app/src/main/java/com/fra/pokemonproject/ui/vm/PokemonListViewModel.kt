package com.fra.pokemonproject.ui.vm

import android.util.Log
import androidx.lifecycle.*
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
    val allPokemonWrapper : LiveData<PokemonResponseWrapper> = _allPokemon //espongo la lista per l'activity

    init {
        Log.d("PokemonListViewModel", "init called")
        getAllPokemon()
    }

    fun getAllPokemon() = viewModelScope.launch {
        try {
            Log.d("PokemonListViewModel", "trying getAllPokemon")
            _allPokemon.postValue(PokemonResponseWrapper("OK", pokemonRepository.getAll()))
            //se la getAll va in OK posto la response che verrá catturata dall'observer nell'activity
        } catch (e: Exception) {
            Log.d("PokemonListViewModel", String.format("getAllPokemon KO - exception: %s", e.message))
            _allPokemon.postValue(PokemonResponseWrapper("KO"))
            //se la getAll va in KO posto la response che verrá catturata dall'observer nell'activity
        }
    }
}