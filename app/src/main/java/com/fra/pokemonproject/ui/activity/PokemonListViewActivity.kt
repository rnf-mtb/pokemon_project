package com.fra.pokemonproject.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fra.pokemonproject.databinding.ActivityLayoutBinding
import com.fra.pokemonproject.model.Pokemon
import com.fra.pokemonproject.model.PokemonResponseWrapper
import com.fra.pokemonproject.ui.adapter.PokemonListAdapter
import com.fra.pokemonproject.ui.adapter.PokemonListAdapterListener
import com.fra.pokemonproject.ui.vm.PokemonListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PokemonListViewActivity : AppCompatActivity(), PokemonListAdapterListener {
    val pkmnVM : PokemonListViewModel by viewModels()
    private lateinit var activityListBinding: ActivityLayoutBinding
    @Inject lateinit var pokemonListAdapter: PokemonListAdapter

    var pokemonList = MutableLiveData<PokemonResponseWrapper>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("PokemonListViewActivity", "onCreate")
        /*
        - istanziare il viewModel dal quale arriva la response
        - istanziare un observer in ascolto sulla response
        - quando la response arriva, setto l'adapter
        - quando l'adapter é settato, popolo la recyclerview
         */

        activityListBinding = ActivityLayoutBinding.inflate(layoutInflater)
        setContentView(activityListBinding.root)

        activityListBinding.pokemonList.layoutManager = LinearLayoutManager(this);

        pkmnVM.allPokemonWrapper.observe(this, Observer { response ->
            Log.d("PokemonListViewActivity", "onCreate - waiting")
            when (response.status){
                "OK" -> response.pokemonResponse?.results?.let{
                    for (pokemon in it){

                    }
                    Log.d("PokemonListViewActivity", "onCreate - OK")
                    pokemonListAdapter.setPokemonList(it, this)
                    Toast.makeText(this, "OK", Toast.LENGTH_LONG).show()
                }
                "KO" -> {
                    Log.d("PokemonListViewActivity", "onCreate - KO")
                    Toast.makeText(this, "KO", Toast.LENGTH_LONG).show()
                }
            }
        })
        activityListBinding.pokemonList.apply {
            adapter = pokemonListAdapter
            Log.d("PokemonListViewActivity", String.format("onCreate - pokemonListAdapter size is %d", activityListBinding.pokemonList.size))
            setHasFixedSize(true)
        }

        activityListBinding.pokemonList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Log.d("PokemonListViewActivity", String.format("recyclerView scrolled newState %s", newState))
            }
        })

        activityListBinding.swipeRefreshLayout.setOnRefreshListener {
            Log.d("PokemonListViewActivity", "refresh recyclerView triggered")
            pkmnVM.allPokemonWrapper
            activityListBinding.swipeRefreshLayout.isRefreshing = false
            Log.d("PokemonListViewActivity", "refresh recyclerView stopped")
        }

        //activityListBinding.swipeRefreshLayout.isRefreshing = true

        activityListBinding.fab.setOnClickListener{
            pkmnVM.getAllPokemon()
        }
    }

    override fun onItemClick(pkmn: Pokemon) {
        Toast.makeText(this, "ITEM CLICKED", Toast.LENGTH_LONG).show()
    }
}
