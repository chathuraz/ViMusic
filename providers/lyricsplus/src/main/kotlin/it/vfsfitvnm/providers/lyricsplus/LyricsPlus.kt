@file:Suppress("JSON_FORMAT_REDUNDANT")

package it.vfsfitvnm.providers.lyricsplus

import it.vfsfitvnm.providers.lyricsplus.models.LyricLine
import it.vfsfitvnm.providers.lyricsplus.models.LyricWord
import it.vfsfitvnm.providers.lyricsplus.models.LyricsResponse
import io.ktor.client.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object LyricsPlus {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun fetchLyrics(
        baseUrl: String,
        title: String,
        artist: String,
        album: String? = null,
    ): List<LyricLine>? {
        val url = "$baseUrl/v2/lyrics/get"

        try {
            // First attempt: Request lyrics with the album
            val response = makeLyricsRequest(url, title, artist, album)

            // Check if the response is valid but contains only line-level sync
            val isLineLevelOnly = response.lyrics.isNotEmpty() && response.lyrics.all { it.syllabus.isEmpty() }

            // If we got line-level sync and we used an album, try again without it
            if (isLineLevelOnly && album != null) {
                // Second attempt: Request lyrics without the album
                val fallbackResponse = makeLyricsRequest(url, title, artist, null)
                return mapResponseToLyricLines(fallbackResponse)
            }

            // If the first response was good (word-level or no album was used), return it
            return mapResponseToLyricLines(response)

        } catch (e: ClientRequestException) {
            // If it fails with a 404 and an album was provided, try again without it
            if (e.response.status == HttpStatusCode.NotFound && album != null) {
                return try {
                    // Second attempt after 404: Request lyrics without the album
                    val fallbackResponse = makeLyricsRequest(url, title, artist, null)
                    mapResponseToLyricLines(fallbackResponse)
                } catch (_: Exception) {
                    null // Return null if the second attempt also fails
                }
            } else {
                return null // Return null for other HTTP errors
            }
        } catch (_: Exception) {
            // Return null for any other type of exception (e.g., network issues)
            return null
        }
    }

    private suspend fun makeLyricsRequest(
        url: String,
        title: String,
        artist: String,
        album: String?
    ): LyricsResponse {
        val response: HttpResponse = client.get(url) {
            parameter("title", title)
            parameter("artist", artist)
            // Ktor ignores null parameters, so this is safe
            parameter("album", album)
        }

        val responseText = response.bodyAsText()

        return Json { ignoreUnknownKeys = true }
            .decodeFromString<LyricsResponse>(responseText)
    }

    private fun mapResponseToLyricLines(response: LyricsResponse): List<LyricLine> {
        return response.lyrics.map { line ->
            LyricLine(
                fullText = line.text,
                startTimeMs = line.time,
                durationMs = line.duration,
                words = line.syllabus.map {
                    LyricWord(
                        text = it.text,
                        startTimeMs = it.time,
                        durationMs = it.duration
                    )
                }
            )
        }
    }
}
