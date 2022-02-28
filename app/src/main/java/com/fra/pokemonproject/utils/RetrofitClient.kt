package com.fra.pokemonproject.utils

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class RetrofitClient {
    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String? = ""): Retrofit? {
        Log.d("RetrofitClient", "baseUrl is $baseUrl")
        if (retrofit == null) {
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        }
        Log.d("RetrofitClient", "retrofit is null? ${retrofit == null}")
        return retrofit
    }
}