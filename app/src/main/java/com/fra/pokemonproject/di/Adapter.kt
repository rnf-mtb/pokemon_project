package com.fra.pokemonproject.di

import com.fra.pokemonproject.ui.adapter.PokemonListAdapter
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
}