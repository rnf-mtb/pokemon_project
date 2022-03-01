package com.fra.pokemonproject.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fra.pokemonproject.R
import com.fra.pokemonproject.databinding.ActivityLayoutBinding
import com.fra.pokemonproject.model.Pokemon
import com.fra.pokemonproject.model.PokemonResponseWrapper
import com.fra.pokemonproject.ui.adapter.PaginationScrollListener
import com.fra.pokemonproject.ui.adapter.PokemonListAdapter
import com.fra.pokemonproject.ui.adapter.PokemonListAdapterListener
import com.fra.pokemonproject.ui.fragment.PokemonDetailFragment
import com.fra.pokemonproject.ui.fragment.PokemonListFragment
import com.fra.pokemonproject.ui.vm.NavigationEvent
import com.fra.pokemonproject.ui.vm.PokemonListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@ExperimentalStdlibApi
@AndroidEntryPoint
class PokemonListViewActivity : AppCompatActivity(){
    companion object {
        val TAG = "PokemonListViewActivity"
    }
    private lateinit var activityBinding: ActivityLayoutBinding
    val pkmnVM : PokemonListViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityLayoutBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        pkmnVM.eventWrapper.observe(this, {
            when(it){
                is NavigationEvent.Home -> { goHome(it.pkmn.name) }
                is NavigationEvent.List -> { goToList(it.pkmn.name) }
                is NavigationEvent.Detail -> { goToDetail(it.pkmn) }
                else -> {}
            }
        })

        goToList("")
    }

    override fun onResume() {
        super.onResume()
        goToList("")
    }


    private fun goToList(pkmnName: String){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, PokemonListFragment(), PokemonListFragment.TAG)
            .commit()
    }

    private fun goHome(pkmnName: String){
        /*supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, PokemonListFragment(), TAG)
            .commit()*/
    }

    private fun goToDetail(pkmn: Pokemon){
        //Toast.makeText(this, "going to detail for ${pkmn.name}", Toast.LENGTH_SHORT).show()
        val fragment = PokemonDetailFragment()
        val bundle = Bundle()
        bundle.putParcelable(PokemonDetailFragment.DETAIL, pkmn)
        fragment.arguments = bundle
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, fragment, PokemonDetailFragment.TAG)
            .commit()
    }

}
