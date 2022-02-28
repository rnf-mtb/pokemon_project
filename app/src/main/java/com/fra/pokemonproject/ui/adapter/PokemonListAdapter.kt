package com.fra.pokemonproject.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fra.pokemonproject.databinding.PokemonItemListBinding
import com.fra.pokemonproject.model.Pokemon
import android.view.View
import com.fra.pokemonproject.R
import com.fra.pokemonproject.databinding.LoadingItemListBinding

import android.widget.ProgressBar
import com.bumptech.glide.Glide

class PokemonListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "PokemonListAdapter"
    private var _pkmnList = mutableListOf<Pokemon>()
    private var _pkmnListAdapterListener : PokemonListAdapterListener? = null
    private val LOADING = 0
    private val ITEM = 1
    private var isLoadingAdded = false

    inner class PokemonViewHolder (
        @NonNull val pokemonItemActivityListBinding: PokemonItemListBinding
    ) : RecyclerView.ViewHolder(pokemonItemActivityListBinding.root) {

        fun bind(pkmn: Pokemon){
            Log.d(TAG, String.format("bind"))
            pokemonItemActivityListBinding.apply {
                val img = Glide.with(this.img.context).load(pkmn.sprites?.front_default).into(this.img)
                this.name.text = pkmn.name
                this.url.text = pkmn.sprites?.front_default
                this.pokemonRow.setOnClickListener{ _pkmnListAdapterListener?.onItemClick(pkmn) }
            }
        }
    }

    inner class LoadingViewHolder (
        @NonNull val loadingItemActivityListBinding: LoadingItemListBinding
    ) : RecyclerView.ViewHolder(loadingItemActivityListBinding.root) {
        val progressBar: ProgressBar = loadingItemActivityListBinding.progressbar

        fun bind(){
            Log.d(TAG, String.format("LoadingViewHolder bind"))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d(TAG, "onCreateViewHolder")

        return when (viewType) {
            ITEM -> {
                Log.d(TAG, "onCreateViewHolder ITEM CREATED")
                PokemonViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                    R.layout.pokemon_item_list, parent, false))
            }
            else -> {
                Log.d(TAG, "onCreateViewHolder LOADING CREATED")
                LoadingViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                    R.layout.loading_item_list, parent, false))
            }
        }
    }

    override fun getItemCount(): Int = _pkmnList.size

    fun setPokemonList(pkmnList: List<Pokemon>, page: Int, pokemonListAdapterListener: PokemonListAdapterListener) {
        Log.d(TAG, String.format("setPokemonList"))
        _pkmnListAdapterListener = pokemonListAdapterListener
        Log.d(TAG, "pkmnList size ${_pkmnList.size}")
        if(page == 0) {
            //_pkmnList.clear()
        } else {
            _pkmnList.clear()
            removeLoadingFooter()
        }
        //_pkmnList.clear()
        _pkmnList.addAll(pkmnList)
        Log.d(TAG, "notifyDataSetChanged done _pkmnList size is ${_pkmnList.size}")
        addLoadingFooter()

        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder")
        val pkmn: Pokemon = _pkmnList[position]
        when (getItemViewType(position)) {
            ITEM -> {
                Log.d(TAG, "onCreateViewHolder ITEM BOUND")
                val pokemonViewHolder: PokemonViewHolder = holder as PokemonViewHolder
                pokemonViewHolder.bind(pkmn)
            }
            LOADING -> {
                Log.d(TAG, "onCreateViewHolder LOADING BOUND")
                val loadingViewHolder = holder as LoadingViewHolder
                loadingViewHolder.progressBar.visibility = View.VISIBLE
                loadingViewHolder.bind()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == _pkmnList.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(Pokemon("", "", null))
        Log.d(TAG, "addLoadingFooter pkmnList ${_pkmnList.size}")
    }

    fun removeLoadingFooter() {
        Log.d(TAG, "removeLoadingFooter pkmnList ${_pkmnList.size}")
        isLoadingAdded = false
        val footerPosition: Int = _pkmnList.size - 1
        if(getItem(footerPosition) != null) {
            _pkmnList.removeAt(footerPosition)
            notifyItemRemoved(footerPosition)
        }
    }

    fun add(pkmn: Pokemon) {
        _pkmnList.add(pkmn)
        notifyItemInserted(_pkmnList.size - 1)
    }

    fun addAll(results: List<Pokemon>) {
        for (result in results) {
            add(result)
        }
    }

    fun clear(){
        Log.d(TAG, String.format("clearing"))
        _pkmnList.clear()
        Log.d(TAG, "_pkmnList size ${_pkmnList.size} ")
    }

    private fun getItem(position: Int): Pokemon? {
        Log.d(TAG, String.format("position %d listSize %d", position, _pkmnList.size))
        return if(position > 0 && _pkmnList.size -1 >= position) { //last item
            _pkmnList[position]
        }
        else null
    }
}
