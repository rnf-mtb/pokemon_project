package com.fra.pokemonproject.db

import androidx.annotation.Nullable
import com.fra.pokemonproject.constant.ConstantURL
import com.fra.pokemonproject.dao.RemotePokemonDAO
import com.fra.pokemonproject.utils.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RemoteDatabase {

    @Provides
    @Singleton
    @Nullable
    fun providePokemonDAO(): RemotePokemonDAO? =
        RetrofitClient().getClient(ConstantURL.POKEMON_API_ENDPOINT)?.create(RemotePokemonDAO::class.java)
}
