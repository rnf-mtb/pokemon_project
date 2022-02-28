package com.fra.pokemonproject.utils

import android.content.Context
import android.content.SharedPreferences
import com.fra.pokemonproject.model.Pokemon

class SessionDataManager {
    private lateinit var _persistor : SharedPreferences
    private var _pkmnGenericMap = mutableMapOf<Int, MutableList<Pokemon>>()

    fun init(context: Context){
        _persistor = context.getSharedPreferences("SessionDataManagerPersistor", Context.MODE_PRIVATE)
    }

    fun getPkmnGenericMap() : String  { return "" }
    fun setPkmnGenericMap(page: Int, pkmns: MutableList<Pokemon>) {

        val tempPkmnMapJson = getPkmnGenericMap()

        /*tempMap[page] = pkmns
        _persistor.edit().put*/
    }
    fun addPkmnGenericMap(page: Int, pkmn: MutableList<Pokemon>) {
        _pkmnGenericMap[page] = pkmn //la key é il nome del pokemon
    }
}