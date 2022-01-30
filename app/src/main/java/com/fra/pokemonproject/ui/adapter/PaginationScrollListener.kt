package com.fra.pokemonproject.ui.adapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView

import androidx.recyclerview.widget.LinearLayoutManager
import com.fra.pokemonproject.constant.ConstantURL.POKEMON_API_PAGE_SIZE


abstract class PaginationScrollListener(private val layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) { //deve dirmi quando arrivo in fondo alla pagina
        super.onScrolled(recyclerView, dx, dy)

    }

    protected abstract fun loadMoreItems()
    abstract var isLastPage: Boolean
    abstract var isLoading: Boolean

}