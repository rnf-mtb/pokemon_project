package com.fra.pokemonproject.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fra.pokemonproject.R
import com.fra.pokemonproject.databinding.PokemonAtkInfoBinding
import com.fra.pokemonproject.model.Moves
import com.fra.pokemonproject.model.PokemonStats

class PokemonAtkListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG = "PokemonAtkAdapter"
    private var _pkmnInfoList = mutableListOf<Moves>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d(TAG, "onCreateViewHolder ITEM CREATED")
        return GenericInfoItemViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.pokemon_atk_info, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder")
        val info: String = _pkmnInfoList[position].move?.name ?: "n.d."
        val pokemonViewHolder: GenericInfoItemViewHolder = holder as GenericInfoItemViewHolder
        pokemonViewHolder.bind(info)
    }

    override fun getItemCount(): Int {
        return _pkmnInfoList.size
    }

    inner class GenericInfoItemViewHolder (
        @NonNull val genericPokemonInfoBinding: PokemonAtkInfoBinding
    ) : RecyclerView.ViewHolder(genericPokemonInfoBinding.root) {

        fun bind(info: String){
            Log.d(TAG, String.format("bind"))
            genericPokemonInfoBinding.apply {
                this.info.text = info
            }
        }
    }

    fun clear(){
        Log.d(TAG, String.format("clearing"))
        _pkmnInfoList.clear()
        Log.d(TAG, "_pkmnInfoList size ${_pkmnInfoList.size} ")
    }

    fun setAtkList(info: List<Moves>){
        _pkmnInfoList.clear()
        _pkmnInfoList.addAll(info)
        notifyDataSetChanged()
    }
}