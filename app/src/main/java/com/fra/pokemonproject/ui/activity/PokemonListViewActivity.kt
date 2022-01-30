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
import com.fra.pokemonproject.ui.adapter.PaginationScrollListener
import com.fra.pokemonproject.ui.adapter.PokemonListAdapter
import com.fra.pokemonproject.ui.adapter.PokemonListAdapterListener
import com.fra.pokemonproject.ui.vm.PokemonListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@AndroidEntryPoint
class PokemonListViewActivity : AppCompatActivity(), PokemonListAdapterListener {
    val pkmnVM : PokemonListViewModel by viewModels()
    private lateinit var activityListBinding: ActivityLayoutBinding
    @Inject lateinit var pokemonListAdapter: PokemonListAdapter

    lateinit var _responseWrapper : PokemonResponseWrapper
    var _pkmnList : MutableList<Pokemon> = mutableListOf()
    var _currentPage = 0
    var _isLoading = false
    var _isLastPage = false
    val _maxPageSize = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _pkmnList = mutableListOf()
        activityListBinding = ActivityLayoutBinding.inflate(layoutInflater)
        setContentView(activityListBinding.root)

        val linearLayoutManager = LinearLayoutManager(this)
        activityListBinding.pokemonList.layoutManager = linearLayoutManager

        pkmnVM.pokemonWrapper.observe(this, { resp ->
            Log.d("PokemonListViewActivity", "getting Pokemon, setting isLoading to false")
            _isLoading = false
            fillListWithSinglePokemonInfo(resp)
        })

        pkmnVM.allPokemonWrapper.observe(this, Observer { response ->
            _responseWrapper = response
            when (response.status){
                "OK" -> response.pokemonResponse?.results?.let{
                    Toast.makeText(this, "OK", Toast.LENGTH_LONG).show()
                    if(it.isEmpty()) {
                        _isLoading = false
                    }

                    _pkmnList.addAll(it)
                    Log.d("PokemonListViewActivity", String.format("pkmnList %d", _pkmnList.size))

                    for (pokemon in it){
                        _isLoading = true
                        Log.d("PokemonListViewActivity", String.format("calling getPokemon %s", pokemon.name))
                        pkmnVM.getSinglePokemon(pokemon.name)
                    }
                    /*Log.d("OnCreate", "starting async fun")
                    GlobalScope.launch(Dispatchers.IO) {
                        it.forEachIndexed { index, pkmn ->
                            println("NOW ${index}")
                            var time = measureTimeMillis {
                                val fn = async {
                                    pkmnVM.getSinglePokemon(pkmn.name)
                                }
                                val result = fn.await()
                                Log.d("OnCreate", "async fun")
                            }
                            Log.d("OnCreate", String.format("async fun time elapsed %d", time))
                        }
                    }*/

                    _isLastPage = response.pokemonResponse.next.isNullOrBlank()
                }
                "KO" -> {
                    _isLoading = false
                    Toast.makeText(this, "KO", Toast.LENGTH_LONG).show()
                }
            }
        })

        activityListBinding.pokemonList.apply {
            pokemonListAdapter.clear()
            adapter = pokemonListAdapter
            setHasFixedSize(true)
        }

        activityListBinding.pokemonList.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0) {
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = _pkmnList.size
                    val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()

                    Log.d("PaginationScrollListener", String.format("!isLoading %b !isLastPage %b", !isLoading, !isLastPage))
                    Log.d("PaginationScrollListener", String.format("visibleItemCount %d and firstVisibleItemPosition %d totalItemCount %d",
                            visibleItemCount, firstVisibleItemPosition, totalItemCount
                        )
                    )

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                        loadMoreItems();
                    }
                }
            }

            override fun loadMoreItems() { //qui ho visibilitá sullo stato delle chiamate
                Log.d("PokemonListViewActivity", String.format("loadMoreItems !isLoading %b !isLastPage %b", !isLoading, !isLastPage))
                if(!_isLoading && !_isLastPage) {
                    _isLoading = true
                    Log.d("PokemonListViewActivity", String.format("loadMoreItems !isLoading %b", !isLoading))
                    loadNextPage()
                }
            }

            override var isLastPage: Boolean = false
            override var isLoading: Boolean = false
        })

/*        activityListBinding.swipeRefreshLayout.setOnRefreshListener {
            pkmnVM.allPokemonWrapper
            activityListBinding.swipeRefreshLayout.isRefreshing = false
        }*/

        //activityListBinding.swipeRefreshLayout.isRefreshing = true

        activityListBinding.fab.setOnClickListener{
            pkmnVM.getSinglePokemon("bulbasaur")
        }
    }

    private fun fillListWithSinglePokemonInfo(resp: PokemonResponseWrapper?) {
        when (resp?.status) {
            "OK" -> resp.pokemonResponse?.results?.let { singlePokemon ->
                Log.d(
                    "PokemonListViewActivity", String.format(
                        "get pokemon %s url %s",
                        singlePokemon.firstOrNull()?.name,
                        singlePokemon.firstOrNull()?.sprites?.front_default
                    )
                )
                Toast.makeText(this, "OK", Toast.LENGTH_LONG).show()

                if (_pkmnList.find { it.name == singlePokemon.firstOrNull()?.name } != null) {
                    Log.d(
                        "PokemonListViewActivity",
                        String.format("pokemon %s found", singlePokemon.firstOrNull()?.name)
                    )
                    _pkmnList.find { it.name == singlePokemon.firstOrNull()?.name }.let {
                        it?.sprites = singlePokemon.firstOrNull()?.sprites
                        pokemonListAdapter.setPokemonList(_pkmnList, _currentPage, this)
                    }
                } else {
                    Log.d(
                        "PokemonListViewActivity",
                        String.format("pokemon %s not found", singlePokemon.firstOrNull()?.name)
                    )
                }
            }
            "KO" -> {
                Log.d("PokemonListViewActivity", "onCreate - get single pokemon KO")
                Toast.makeText(this, "KO", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun loadNextPage() {
        _currentPage += 1
        Log.d("PokemonListViewActivity", String.format("loadNextPage"))
        pkmnVM.getAllPokemon(_currentPage)
    }

    private fun loadFirstPage() {
        Log.d("PokemonListViewActivity", String.format("loadFirstPage"))
        if(!_isLoading)
            pkmnVM.getAllPokemon(0)
    }

    override fun onItemClick(pkmn: Pokemon) {
        Toast.makeText(this, "ITEM CLICKED", Toast.LENGTH_LONG).show()
    }
}
