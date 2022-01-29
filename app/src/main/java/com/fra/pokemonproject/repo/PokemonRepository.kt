package com.fra.pokemonproject.repo

import com.fra.pokemonproject.dao.RemotePokemonDAO
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.components.SingletonComponent
import retrofit2.http.GET
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class PokemonRepository @Inject constructor(
    private val pokemonDAO: RemotePokemonDAO?
) {
    suspend fun getPokemonFromInfo(info : String) = pokemonDAO?.getPokemonFromInfo(info)
    suspend fun getAll() = pokemonDAO?.getAll()
}