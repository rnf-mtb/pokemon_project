package com.fra.pokemonproject.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fra.pokemonproject.databinding.PokemonListBinding
import com.fra.pokemonproject.model.Pokemon
import com.fra.pokemonproject.model.PokemonResponseWrapper
import com.fra.pokemonproject.ui.adapter.PaginationScrollListener
import com.fra.pokemonproject.ui.adapter.PokemonListAdapter
import com.fra.pokemonproject.ui.adapter.PokemonListAdapterListener
import com.fra.pokemonproject.ui.vm.NavigationEvent
import com.fra.pokemonproject.ui.vm.PokemonListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

@ExperimentalStdlibApi
@AndroidEntryPoint
class PokemonListFragment : Fragment(), PokemonListAdapterListener {
    companion object{ val TAG = "PokemonListFragment" }

    val pkmnVM : PokemonListViewModel by activityViewModels()
    @Inject lateinit var pokemonListAdapter: PokemonListAdapter
    private var _binding: PokemonListBinding? = null
    private val binding get() = _binding!!
    lateinit var _responseWrapper : PokemonResponseWrapper
    var _pkmnList : MutableList<Pokemon> = mutableListOf()
    var _currentPage = 0
    var _isLoading = false
    var _isLastPage = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PokemonListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _pkmnList.clear()

        val linearLayoutManager = LinearLayoutManager(context)
        binding.pokemonList.layoutManager = linearLayoutManager

        pkmnVM.pokemonWrapper.observe(this, { resp ->
            _isLoading = false
            fillListWithListPokemonInfo(resp)
        })

        pkmnVM.allPokemonWrapper.observe(this, Observer { response ->
            _responseWrapper = response
            when (response.status){
                "OK" -> response.pokemonResponse?.results?.let { pokemonList ->
                    Toast.makeText(context, "OK response.status", Toast.LENGTH_SHORT).show()
                    if (pokemonList.isEmpty()) {
                        _isLoading = false
                    }

                    _pkmnList.addAll(pokemonList)

                    GlobalScope.launch(Dispatchers.Main) {
                        pkmnVM.getPokemonListMoreInfo(_pkmnList)
                    }
                    _isLastPage = response.pokemonResponse.next.isNullOrBlank()
                }
                "KO" -> {
                    _isLoading = false
                    Toast.makeText(context, "KO response.status", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.pokemonList.apply {
            pokemonListAdapter.clear()
            adapter = pokemonListAdapter
            setHasFixedSize(true)
        }

        binding.pokemonList.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0) {
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = _pkmnList.size
                    val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                        loadMoreItems();
                    }
                }
            }

            override fun loadMoreItems() { //qui ho visibilitá sullo stato delle chiamate
                if(!_isLoading && !_isLastPage) {
                    _isLoading = true
                    loadNextPage()
                }
            }

            override var isLastPage: Boolean = false
            override var isLoading: Boolean = false
        })

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = true
            pkmnVM.getAllPokemon(_currentPage)
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fillListWithListPokemonInfo(resp: PokemonResponseWrapper) {
        when (resp.status) {
            "OK" -> resp.pokemonResponse?.results?.forEach { singlePokemon ->
                Toast.makeText(context, "OK fillListWithSinglePokemonInfo", Toast.LENGTH_SHORT).show()

                if (_pkmnList.find { it.name == singlePokemon.name } != null) {
                    _pkmnList.find { it.name == singlePokemon.name }.let {
                        it?.sprites = singlePokemon.sprites
                        pokemonListAdapter.setPokemonList(_pkmnList, _currentPage, this)
                        it?.moves = singlePokemon.moves
                        it?.stats = singlePokemon.stats
                        it?.types = singlePokemon.types
                    }
                } else {
                    Log.d(TAG, String.format("pokemon %s not found", singlePokemon?.name))
                }
            }
            "KO" -> {
                Toast.makeText(context, "KO fillListWithSinglePokemonInfo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fillListWithSinglePokemonInfo(resp: PokemonResponseWrapper?) {
        when (resp?.status) {
            "OK" -> resp.pokemonResponse?.results?.let { singlePokemon ->
                Toast.makeText(context, "OK fillListWithSinglePokemonInfo", Toast.LENGTH_SHORT).show()

                if (_pkmnList.find { it.name == singlePokemon.firstOrNull()?.name } != null) {
                    _pkmnList.find { it.name == singlePokemon.firstOrNull()?.name }.let {
                        it?.sprites = singlePokemon.firstOrNull()?.sprites
                        pokemonListAdapter.setPokemonList(_pkmnList, _currentPage, this)
                    }
                } else {
                    Log.d(TAG, String.format("pokemon %s not found", singlePokemon.firstOrNull()?.name))
                }
            }
            "KO" -> {
                Toast.makeText(context, "KO fillListWithSinglePokemonInfo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadNextPage() {
        _currentPage += 1
        pkmnVM.getAllPokemon(_currentPage)
    }

    override fun onItemClick(pkmn: Pokemon) {
        Toast.makeText(context, String.format("clicked %s", pkmn.name), Toast.LENGTH_SHORT).show()
        pkmnVM.navigate(NavigationEvent.Detail(pkmn))
    }
}