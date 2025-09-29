package it.vfsfitvnm.providers.github.requests

import it.vfsfitvnm.providers.github.GitHub
import it.vfsfitvnm.providers.github.models.Release
import it.vfsfitvnm.providers.utils.runCatchingCancellable
import io.ktor.client.call.body
import io.ktor.client.request.get

suspend fun GitHub.releases(
    owner: String,
    repo: String,
    page: Int = 1,
    pageSize: Int = 30
) = runCatchingCancellable {
    httpClient.get("repos/$owner/$repo/releases") {
        withPagination(page = page, size = pageSize)
    }.body<List<Release>>()
}
