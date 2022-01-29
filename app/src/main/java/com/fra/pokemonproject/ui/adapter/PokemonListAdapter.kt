package com.fra.pokemonproject.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fra.pokemonproject.R
import com.fra.pokemonproject.databinding.PokemonItemListBinding
import com.fra.pokemonproject.model.Pokemon





class PokemonListAdapter : RecyclerView.Adapter<PokemonListAdapter.ViewHolder>() {

    private var _pkmnList = mutableListOf<Pokemon>()
    private var _pkmnListAdapterListener : PokemonListAdapterListener? = null

    inner class ViewHolder (
        @NonNull val pokemonItemActivityListBinding: PokemonItemListBinding
    ) : RecyclerView.ViewHolder(pokemonItemActivityListBinding.root) {

        fun bind(pkmn: Pokemon){
            Log.d("PokemonListAdapter", String.format("bind"))
            pokemonItemActivityListBinding.apply {
                Log.d("PokemonListAdapter", String.format("binding pokemon %s", pkmn.name))
                //this.pokemon = pkmn
                this.name.text = pkmn.name
                this.url.text = pkmn.url
                this.pokemonRow.setOnClickListener{ _pkmnListAdapterListener?.onItemClick(pkmn) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("PokemonListAdapter", "onCreateViewHolder")
        return ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.pokemon_item_list, parent, false))

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("PokemonListAdapter", "onBindViewHolder")
        holder.bind(_pkmnList[position])
    }

    override fun getItemCount(): Int = _pkmnList.size

    fun setPokemonList(pkmnList: List<Pokemon>, pokemonListAdapterListener: PokemonListAdapterListener) {
        Log.d("PokemonListAdapter", String.format("setPokemonList"))
        _pkmnList = pkmnList as MutableList<Pokemon>
        _pkmnListAdapterListener = pokemonListAdapterListener
        Log.d("PokemonListAdapter", String.format("pkmnList size %d", _pkmnList.size))
        if(pkmnList != _pkmnList) {
            _pkmnList.clear()
            _pkmnList.addAll(pkmnList)
            Log.d("PokemonListAdapter", String.format("setPokemonList %d", _pkmnList.size))
        }
        notifyDataSetChanged()
        Log.d("PokemonListAdapter", String.format("notifyDataSetChanged done"))
    }
    //devo mappare la response in un oggetto facilmente visualizzabile
}
