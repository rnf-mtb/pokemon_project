package com.fra.pokemonproject.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.fra.pokemonproject.databinding.PokemonDetailBinding
import com.fra.pokemonproject.model.Pokemon
import com.fra.pokemonproject.ui.adapter.PokemonAtkListAdapter
import com.fra.pokemonproject.ui.adapter.PokemonStatsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class PokemonDetailFragment : Fragment() {
    companion object {
        val TAG = "PokemonDetailFragment"
        val DETAIL = "DETAIL"
    }
    private var _binding: PokemonDetailBinding? = null
    private var _pkmn = Pokemon()
    @Inject
    lateinit var pokemonAtkListAdapter: PokemonAtkListAdapter
    @Inject
    lateinit var pokemonStatsListAdapter: PokemonStatsListAdapter

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PokemonDetailBinding.inflate(inflater, container, false)
        arguments.let {
            _pkmn = it?.getParcelable(DETAIL) ?: Pokemon()
        }
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalScope.launch(Dispatchers.Main) {
            setImage(_pkmn.sprites?.front_default, binding.img1)
            setImage(_pkmn.sprites?.back_default, binding.img2)
            setImage(_pkmn.sprites?.front_shiny, binding.img3)
            setImage(_pkmn.sprites?.back_shiny, binding.img4)
        }

        binding.name.text = "Name: ${_pkmn.name}"

        var types = ""
        _pkmn.types?.forEach { type -> types += "${type.type?.name}, " }
        binding.type.text = "Types: ${types.substring(0, types.trim().length-1)}"


        binding.atksRv.apply{
            pokemonAtkListAdapter.setAtkList(_pkmn.moves ?: listOf())
            adapter = pokemonAtkListAdapter
            setHasFixedSize(true)
        }
        binding.statsRv.apply{
            pokemonStatsListAdapter.setStatsList(_pkmn.stats ?: listOf())
            adapter = pokemonStatsListAdapter
            setHasFixedSize(true)
        }

        binding.backButton.setOnClickListener {
             parentFragmentManager.beginTransaction().remove(this).commit()
        }
    }

    private suspend fun setImage(pkmnImg: String?, imgView: ImageView) {
        withContext(Dispatchers.Main) {
            Glide.with(imgView.context).load(pkmnImg).into(imgView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}