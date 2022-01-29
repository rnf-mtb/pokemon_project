package com.fra.pokemonproject.ui.adapter

import com.fra.pokemonproject.model.Pokemon

interface PokemonListAdapterListener {
    fun onItemClick(pkmn: Pokemon)
}