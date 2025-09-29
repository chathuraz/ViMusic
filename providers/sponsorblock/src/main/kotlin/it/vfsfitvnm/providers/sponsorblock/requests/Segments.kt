package it.vfsfitvnm.providers.sponsorblock.requests

import it.vfsfitvnm.providers.sponsorblock.SponsorBlock
import it.vfsfitvnm.providers.sponsorblock.models.Action
import it.vfsfitvnm.providers.sponsorblock.models.Category
import it.vfsfitvnm.providers.sponsorblock.models.Segment
import it.vfsfitvnm.providers.utils.SerializableUUID
import it.vfsfitvnm.providers.utils.runCatchingCancellable
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

suspend fun SponsorBlock.segments(
    videoId: String,
    categories: List<Category>? = listOf(Category.Sponsor, Category.OfftopicMusic, Category.PoiHighlight),
    actions: List<Action>? = listOf(Action.Skip, Action.POI),
    segments: List<SerializableUUID>? = null
) = runCatchingCancellable {
    httpClient.get("/api/skipSegments") {
        parameter("videoID", videoId)
        if (!categories.isNullOrEmpty()) categories.forEach { parameter("category", it.serialName) }
        if (!actions.isNullOrEmpty()) actions.forEach { parameter("action", it.serialName) }
        if (!segments.isNullOrEmpty()) segments.forEach { parameter("requiredSegment", it) }
        parameter("service", "YouTube")
    }.body<List<Segment>>()
}
