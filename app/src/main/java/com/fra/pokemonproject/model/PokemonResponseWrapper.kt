package com.fra.pokemonproject.model
import android.media.Image
import android.os.Parcelable
import android.webkit.WebSettings
import android.widget.ImageView
import androidx.compose.ui.graphics.ImageBitmap
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fra.pokemonproject.R
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
) : Parcelable {
/*
    companion object {

        @JvmStatic
        @BindingAdapter("image")
        fun loadImage(imageView: ImageView, url: String) {
            Glide.with(imageView.context)
                .load(
                    GlideUrl(url,
                        LazyHeaders.Builder()
                            .addHeader("User-Agent", WebSettings.getDefaultUserAgent(imageView.context))
                            .build()
                    )
                )
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transition(DrawableTransitionOptions.withCrossFade())
                .fitCenter()
                .placeholder(R.color.cardview_light_background)
                .error(R.color.cardview_dark_background)
                .into(imageView)
        }
    }*/
}

@Parcelize
data class Pokemon(
    val name : String = "",
    val url : String = "",
    var sprites : Aspect? = null
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
