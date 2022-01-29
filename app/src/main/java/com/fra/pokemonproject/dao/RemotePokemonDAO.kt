package com.fra.pokemonproject.dao

import com.fra.pokemonproject.model.Pokemon
import com.fra.pokemonproject.model.PokemonResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface RemotePokemonDAO {
    @GET("{info}/")
    suspend fun getPokemonFromInfo(@Path("info") info: String) : Pokemon

    @GET("pokemon/")
    suspend fun getAll() : PokemonResponse
}