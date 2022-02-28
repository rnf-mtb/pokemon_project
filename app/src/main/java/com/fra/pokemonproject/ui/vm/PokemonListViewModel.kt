package com.fra.pokemonproject.ui.vm

import android.se.omapi.Session
import android.util.Log
import androidx.lifecycle.*
import com.fra.pokemonproject.model.Pokemon
import com.fra.pokemonproject.model.PokemonDetailState
import com.fra.pokemonproject.model.PokemonResponse
import com.fra.pokemonproject.model.PokemonResponseWrapper
import com.fra.pokemonproject.repo.PokemonRepository
import com.fra.pokemonproject.ui.event.PokemonEvent
import com.fra.pokemonproject.utils.SessionDataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.uniflow.android.AndroidDataFlow
import kotlinx.coroutines.*
import javax.inject.Inject

@ExperimentalStdlibApi
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : AndroidDataFlow(), LifecycleObserver {
    private val _allPokemon = MutableLiveData<PokemonResponseWrapper>()
    val allPokemonWrapper : LiveData<PokemonResponseWrapper> = _allPokemon
    private val _pokemon = MutableLiveData<PokemonResponseWrapper>()
    val pokemonWrapper : LiveData<PokemonResponseWrapper> = _pokemon

    private val _event = MutableLiveData<NavigationEvent>()
    val eventWrapper : LiveData<NavigationEvent> = _event

    init {
        Log.d("PokemonListViewModel", "init called")
        getAllPokemon(0)
    }

    @ExperimentalStdlibApi
    fun getAllPokemon(page: Int) = viewModelScope.launch {
        try {
            Log.d("PokemonListViewModel", "trying getAllPokemon")
            val response = pokemonRepository.getAllPaged(page) //prende da una mappa in sessione per pagina (page) oppure chiama il servizio
            SessionDataManager.setPkmnGenericMap(page, response?.results ?: listOf())
            _allPokemon.postValue(PokemonResponseWrapper("OK", response, page))
        } catch (e: Exception) {
            Log.d("PokemonListViewModel", "getAllPokemon KO - exception: ${e.message}")
            _allPokemon.postValue(PokemonResponseWrapper("KO"))
        }
    }

    fun getAllPokemon_uniflow(page: Int) = action(
        onAction = {
            val listPokemonResponse = PokemonDetailState(PokemonResponseWrapper("OK", pokemonRepository.getAllPaged(page), page))
            setState(listPokemonResponse)
        },
        onError = { exception, state ->
            Log.d("PokemonListViewModel", String.format("getAllPokemon KO - exception: %s", exception.message))
            sendEvent(PokemonEvent.RetryList(page))
        }
    )

    suspend fun getPokemonListMoreInfo(pokemon: Pokemon) = withContext(Dispatchers.IO) {
        Log.d("PokemonListViewModel", "trying getSinglePokemon")
        try {
            val pkmn = GlobalScope.async {
                pokemonRepository.getPokemonFromInfo(pokemon.name) ?: Pokemon() //va a guardare in sessione se é presente uno dei dati contenuti
            }.await()
            _pokemon.postValue(PokemonResponseWrapper("OK", PokemonResponse(0, null, null, listOf(pkmn))))
        } catch (e: Exception) {
            Log.d("PokemonListViewModel", String.format("getSinglePokemon KO - exception: %s", e.message))
            _pokemon.postValue(PokemonResponseWrapper("KO"))
        }
    }

    suspend fun getPokemonListMoreInfo(pokemon: List<Pokemon>) = withContext(Dispatchers.IO) {
        Log.d("PokemonListViewModel", "trying getSinglePokemon")
        try {
            for(pkmn in pokemon) {
                val pkmn = GlobalScope.async {
                    pokemonRepository.getPokemonFromInfo(pkmn.name) ?: Pokemon() //va a guardare in sessione se é presente uno dei dati contenuti
                }.await()
                _pokemon.postValue(PokemonResponseWrapper("OK", PokemonResponse(0, null, null, listOf(pkmn))))
            }
        } catch (e: Exception) {
            Log.d("PokemonListViewModel", String.format("getSinglePokemon KO - exception: %s", e.message))
            _pokemon.postValue(PokemonResponseWrapper("KO"))
        }
    }

    fun navigate(route: NavigationEvent) = viewModelScope.launch {
        _event.postValue(route)
    }

}

sealed class NavigationEvent {
    data class Home constructor(val pkmn: Pokemon) : NavigationEvent()
    data class List constructor(val pkmn: Pokemon) : NavigationEvent()
    data class Detail constructor(val pkmn: Pokemon) : NavigationEvent()
}

