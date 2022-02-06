package com.fra.pokemonproject.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonResponseWrapper(
    val status: String? = null,
    val pokemonResponse : PokemonResponse? = null,
    val page: Int = 0
) : Parcelable

@Parcelize
data class PokemonResponse (
    val count : Int = 0,
    val next : String? = null,
    val previous : String? = null,
    val results : List<Pokemon>
) : Parcelable

@Parcelize
data class Pokemon(
    val name : String = "",
    val url : String = "",
    var sprites : Aspect? = null,
    var stats : List<PokemonStats>? = null,
    var types : List<PokemonTypes>? = null,
    var localImgPath : String = ""
) : Parcelable

@Parcelize
data class Aspect(
    var back_default: String?,
    var back_female: String?,
    var back_shiny: String?,
    var back_shiny_female: String?,
    var front_default: String?,
    var front_female: String?,
    var front_shiny: String?,
    var front_shiny_female: String?,
) : Parcelable

@Parcelize
data class PokemonStats (
    val base_stat : String = "",
    val effort : Int? = null,
    val stat : PokemonStat? = null
) : Parcelable

@Parcelize
data class PokemonStat (
    val name : String = "",
    val url : String = ""
) : Parcelable

@Parcelize
data class PokemonTypes (
    val slot : Int?,
    val type : PokemonType? = null
) : Parcelable

@Parcelize
data class PokemonType (
    val name : String = "",
    val url : String = ""
) : Parcelable
