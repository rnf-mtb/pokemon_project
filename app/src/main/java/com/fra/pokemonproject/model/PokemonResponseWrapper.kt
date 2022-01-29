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
    val pokemonResponse : PokemonResponse? = null
) : Parcelable

@Parcelize
data class PokemonResponse (
    val count : Int,
    val next : String,
    val previous : String,
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
    val name : String,
    val url : String
) : Parcelable