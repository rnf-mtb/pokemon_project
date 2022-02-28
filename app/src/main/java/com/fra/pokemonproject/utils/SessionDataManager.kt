package com.fra.pokemonproject.utils

import com.fra.pokemonproject.model.Pokemon

class SessionDataManager {
    private var _pkmnMap = mutableMapOf<String, Pokemon>()

    fun getPkmnMap() : MutableMap<String, Pokemon> { return _pkmnMap }
    fun setPkmnMap(map: MutableMap<String, Pokemon>) { _pkmnMap = map }
    fun addPkmnToMap(key: String, pkmn: Pokemon) {
        _pkmnMap[key] = pkmn //la key é il nome del pokemon
    }
}