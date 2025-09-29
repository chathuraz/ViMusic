package it.vfsfitvnm.providers.innertube.models.bodies

import it.vfsfitvnm.providers.innertube.models.Context
import kotlinx.serialization.Serializable

@Serializable
data class ContinuationBody(
    val context: Context = Context.DefaultWeb,
    val continuation: String
)
