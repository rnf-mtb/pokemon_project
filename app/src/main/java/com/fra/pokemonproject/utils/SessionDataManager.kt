package com.fra.pokemonproject.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.fra.pokemonproject.model.Pokemon
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter

object SessionDataManager {

    private val TAG = "SessionDataManager"
    private lateinit var _persistor : SharedPreferences
    private val _moshi = Moshi.Builder().build()
    private var _pkmnGenericMap = mutableMapOf<Int, MutableList<Pokemon>>()
    private val POKEMON_LIST = "POKEMON_LIST"

    fun init(context: Context){
        _persistor = context.getSharedPreferences("SessionDataManagerPersistor", Context.MODE_PRIVATE)
    }

    private fun getPkmnGenericMapJson() : String? {
        return _persistor.getString(POKEMON_LIST, "")
    }

    @ExperimentalStdlibApi
    fun setPkmnGenericMap(page: Int, pkmns: List<Pokemon>) {
        try {
            // restituisce un json
            var tempPkmnMapJson = getPkmnGenericMapJson()

            //converto in mappa
            val jsonAdapter: JsonAdapter<MutableMap<Int, List<Pokemon>>> = _moshi.adapter()
            val map: MutableMap<Int, List<Pokemon>> =
                jsonAdapter.fromJson(tempPkmnMapJson) ?: mutableMapOf()

            //inserisco in coda
            map[page] = pkmns
            tempPkmnMapJson = jsonAdapter.toJson(map)

            //metto in sessione
            _persistor.edit().putString(POKEMON_LIST, tempPkmnMapJson).apply()
        } catch (e : Exception){
            Log.d(TAG, "converting KO - exception: ${e.message}")
        }

    }
}