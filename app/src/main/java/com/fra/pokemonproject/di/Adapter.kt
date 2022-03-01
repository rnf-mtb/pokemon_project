package com.fra.pokemonproject.di

import com.fra.pokemonproject.ui.adapter.PokemonAtkListAdapter
import com.fra.pokemonproject.ui.adapter.PokemonListAdapter
import com.fra.pokemonproject.ui.adapter.PokemonStatsListAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class Adapter {

    @Provides
    @ActivityScoped
    fun providePokemonListAdapter(): PokemonListAdapter = PokemonListAdapter()

    @Provides
    @ActivityScoped
    fun providePokemonAtkListAdapter(): PokemonAtkListAdapter = PokemonAtkListAdapter()

    @Provides
    @ActivityScoped
    fun providePokemonStatsListAdapter(): PokemonStatsListAdapter = PokemonStatsListAdapter()
}