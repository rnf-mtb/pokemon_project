package com.fra.pokemonproject.ui.vm

import android.util.Log
import androidx.lifecycle.*
import com.fra.pokemonproject.model.Pokemon
import com.fra.pokemonproject.model.PokemonResponse
import com.fra.pokemonproject.model.PokemonResponseWrapper
import com.fra.pokemonproject.repo.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel(), LifecycleObserver {
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

    fun getAllPokemon(page: Int) = viewModelScope.launch {
        try {
            Log.d("PokemonListViewModel", "trying getAllPokemon")
            val response = pokemonRepository.getAllPaged(page) //prende da una mappa in sessione per pagina oppure chiama
            _allPokemon.postValue(PokemonResponseWrapper("OK", response, page))
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

    suspend fun getPokemonListMoreInfo(pokemon: List<Pokemon>) = withContext(Dispatchers.IO) {
        Log.d("PokemonListViewModel", "trying getSinglePokemon")
        try {
            val pokemons = mutableListOf<Pokemon>()
                for(pkmn in pokemon) {
                    pokemons.add(
                        GlobalScope.async {
                            pokemonRepository.getPokemonFromInfo(pkmn.name) ?: Pokemon() //va a guardare in sessione se é presente uno dei dati contenuti
                        }.await()
                    )
                }
            _pokemon.postValue(PokemonResponseWrapper("OK", PokemonResponse(0, null, null, pokemons)))
        } catch (e: Exception) {
            Log.d("PokemonListViewModel", String.format("getSinglePokemon KO - exception: %s", e.message))
            _pokemon.postValue(PokemonResponseWrapper("KO"))
        }
    }

    fun navigate(route: NavigationEvent) = viewModelScope.launch {
        _event.postValue(route)
    }

}

/*enum class NavigationEvent(route: String) {
    GO_TO_HOME( "GO_TO_HOME"),
    GO_TO_LIST("GO_TO_LIST"),
    GO_TO_DETAIL( "GO_TO_DETAIL")
}*/

sealed class NavigationEvent {
    data class Home constructor(val pkmn: Pokemon) : NavigationEvent()
    data class List constructor(val pkmn: Pokemon) : NavigationEvent()
    data class Detail constructor(val pkmn: Pokemon) : NavigationEvent()
}

