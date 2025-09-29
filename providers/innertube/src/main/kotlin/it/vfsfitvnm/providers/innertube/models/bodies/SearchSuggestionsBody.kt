package it.vfsfitvnm.providers.innertube.models.bodies

import it.vfsfitvnm.providers.innertube.models.Context
import kotlinx.serialization.Serializable

@Serializable
data class SearchSuggestionsBody(
    val context: Context = Context.DefaultWeb,
    val input: String
)
