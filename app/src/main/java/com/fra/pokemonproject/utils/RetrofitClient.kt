package com.fra.pokemonproject.utils

import android.util.Log
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Retrofit


class RetrofitClient {
    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String? = ""): Retrofit? {
        Log.d("RetrofitClient", "baseUrl is $baseUrl")
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        Log.d("RetrofitClient", "retrofit is null? ${retrofit == null}")
        return retrofit
    }
}