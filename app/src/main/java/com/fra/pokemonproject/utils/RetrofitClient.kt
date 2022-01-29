package com.fra.pokemonproject.utils

import android.util.Log
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Retrofit


class RetrofitClient {
    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String? = ""): Retrofit? {
        Log.d("RetrofitClient", String.format("baseUrl is %s", baseUrl))
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        Log.d("RetrofitClient", String.format("retrofit is null? %b", retrofit == null))
        return retrofit
    }
}