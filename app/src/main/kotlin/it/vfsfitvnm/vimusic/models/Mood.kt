package it.vfsfitvnm.vimusic.models

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import it.vfsfitvnm.core.ui.ColorParceler
import it.vfsfitvnm.providers.innertube.Innertube
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith

@Parcelize
data class Mood(
    val name: String,
    val color: @WriteWith<ColorParceler> Color,
    val browseId: String?,
    val params: String?
) : Parcelable

fun Innertube.Mood.Item.toUiMood() = Mood(
    name = title,
    color = Color(stripeColor),
    browseId = endpoint.browseId,
    params = endpoint.params
)
