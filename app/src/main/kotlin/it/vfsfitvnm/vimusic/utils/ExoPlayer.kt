@file:OptIn(UnstableApi::class)

package it.vfsfitvnm.vimusic.utils

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.PlaybackException
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DataSpec
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.HttpDataSource.InvalidResponseCodeException
import androidx.media3.datasource.ResolvingDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import java.io.EOFException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.math.pow
import kotlin.random.Random

class RangeHandlerDataSourceFactory(private val parent: DataSource.Factory) : DataSource.Factory {
    class Source(private val parent: DataSource) : DataSource by parent {
        override fun open(dataSpec: DataSpec) = runCatching {
            parent.open(dataSpec)
        }.getOrElse { e ->
            if (
                e.findCause<EOFException>() != null ||
                e.findCause<InvalidResponseCodeException>()?.responseCode == 416
            ) parent.open(
                dataSpec
                    .buildUpon()
                    .setHttpRequestHeaders(
                        dataSpec.httpRequestHeaders.filter {
                            !it.key.equals("range", ignoreCase = true)
                        }
                    )
                    .setLength(C.LENGTH_UNSET.toLong())
                    .build()
            )
            else throw e
        }
    }

    override fun createDataSource() = Source(parent.createDataSource())
}

class CatchingDataSourceFactory(
    private val parent: DataSource.Factory,
    private val onError: ((Throwable) -> Unit)?
) : DataSource.Factory {
    inner class Source(private val parent: DataSource) : DataSource by parent {
        override fun open(dataSpec: DataSpec) = runCatching {
            parent.open(dataSpec)
        }.getOrElse { ex ->
            ex.printStackTrace()
            Log.e(TAG, "Error opening data source: ${ex.message}", ex)

            // Handle specific error codes that indicate stream expiry or access issues
            val responseCode = ex.findCause<InvalidResponseCodeException>()?.responseCode
            when (responseCode) {
                403, 404, 410 -> {
                    Log.w(TAG, "Stream access denied or expired (HTTP $responseCode), requesting refresh")
                    throw PlaybackException(
                        "Stream access denied or expired",
                        ex,
                        PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS
                    ).also { onError?.invoke(it) }
                }
                else -> {
                    if (ex is PlaybackException) throw ex
                    else throw PlaybackException(
                        "Unknown playback error",
                        ex,
                        PlaybackException.ERROR_CODE_UNSPECIFIED
                    ).also { onError?.invoke(it) }
                }
            }
        }
    }

    override fun createDataSource() = Source(parent.createDataSource())
}

fun DataSource.Factory.handleRangeErrors(): DataSource.Factory = RangeHandlerDataSourceFactory(this)
fun DataSource.Factory.handleUnknownErrors(
    onError: ((Throwable) -> Unit)? = null
): DataSource.Factory = CatchingDataSourceFactory(
    parent = this,
    onError = onError
)

class FallbackDataSourceFactory(
    private val upstream: DataSource.Factory,
    private val fallback: DataSource.Factory
) : DataSource.Factory {
    inner class Source(private val parent: DataSource) : DataSource by parent {
        override fun open(dataSpec: DataSpec) = runCatching {
            parent.open(dataSpec)
        }.getOrElse { ex ->
            ex.printStackTrace()
            Log.w(TAG, "Primary source failed, trying fallback: ${ex.message}")

            runCatching {
                fallback.createDataSource().open(dataSpec)
            }.getOrElse { fallbackEx ->
                fallbackEx.printStackTrace()
                Log.e(TAG, "Fallback source also failed: ${fallbackEx.message}")
                throw ex
            }
        }
    }

    override fun createDataSource() = Source(upstream.createDataSource())
}

fun DataSource.Factory.withFallback(
    fallbackFactory: DataSource.Factory
): DataSource.Factory = FallbackDataSourceFactory(this, fallbackFactory)

fun DataSource.Factory.withFallback(
    context: Context,
    resolver: ResolvingDataSource.Resolver
) = withFallback(ResolvingDataSource.Factory(DefaultDataSource.Factory(context), resolver))

class RetryingDataSourceFactory(
    private val parent: DataSource.Factory,
    private val maxRetries: Int,
    private val printStackTrace: Boolean,
    private val exponential: Boolean,
    private val predicate: (Throwable) -> Boolean
) : DataSource.Factory {
    inner class Source(private val parent: DataSource) : DataSource by parent {
        override fun open(dataSpec: DataSpec): Long {
            var lastException: Throwable? = null
            var retries = 0
            while (retries <= maxRetries) {
                if (retries > 0) {
                    Log.d(TAG, "Retry $retries of $maxRetries fetching datasource")

                    // Add jitter to prevent thundering herd
                    val baseDelay = if (exponential) 1000L * 2.0.pow(retries - 1).toLong() else 2500L
                    val jitter = Random.nextLong(0, baseDelay / 4)
                    val delay = baseDelay + jitter

                    Log.d(TAG, "Retry policy accepted retry, sleeping for $delay milliseconds")
                    Thread.sleep(delay)
                }

                @Suppress("TooGenericExceptionCaught")
                return try {
                    parent.open(dataSpec)
                } catch (ex: Throwable) {
                    lastException = ex
                    if (printStackTrace) Log.e(
                        TAG,
                        "Exception caught by retry mechanism (attempt ${retries + 1})",
                        ex
                    )

                    // Don't retry certain unrecoverable errors
                    val responseCode = ex.findCause<InvalidResponseCodeException>()?.responseCode
                    if (responseCode in listOf(401, 403, 404, 410)) {
                        Log.e(TAG, "Unrecoverable HTTP error $responseCode, not retrying")
                        throw ex
                    }

                    if (retries >= maxRetries) {
                        Log.e(TAG, "Max retries $maxRetries exceeded, throwing the last exception...")
                        throw ex
                    }

                    if (predicate(ex)) {
                        retries++
                        continue
                    }

                    Log.e(TAG, "Retry policy declined retry, throwing the last exception...")
                    throw ex
                }
            }

            throw lastException!!
        }
    }

    override fun createDataSource() = Source(parent.createDataSource())
}

inline fun <reified T : Throwable> DataSource.Factory.retryIf(
    maxRetries: Int = 3,
    printStackTrace: Boolean = false,
    exponential: Boolean = true
) = retryIf(maxRetries, printStackTrace, exponential) { ex -> ex.findCause<T>() != null }

private const val TAG = "DataSource.Factory"

fun DataSource.Factory.retryIf(
    maxRetries: Int = 3,
    printStackTrace: Boolean = false,
    exponential: Boolean = true,
    predicate: (Throwable) -> Boolean
): DataSource.Factory = RetryingDataSourceFactory(this, maxRetries, printStackTrace, exponential, predicate)

// Enhanced retry policy for common network issues
fun DataSource.Factory.withNetworkRetry(
    maxRetries: Int = 3
): DataSource.Factory = retryIf(maxRetries, true, true) { ex ->
    ex.findCause<SocketTimeoutException>() != null ||
        ex.findCause<UnknownHostException>() != null ||
        (ex.findCause<InvalidResponseCodeException>()?.responseCode in listOf(429, 500, 502, 503, 504))
}

val Cache.asDataSource get() = CacheDataSource.Factory().setCache(this)

val Context.defaultDataSource
    get() = DefaultDataSource.Factory(
        this,
        DefaultHttpDataSource.Factory()
            .setDefaultRequestProperties(
                mapOf(
                    "Range" to "bytes=0-",
                    "Cookie" to "CONSENT=YES+cb",
                    "Referer" to ""
                )
            )
            .setConnectTimeoutMs(20000)
            .setReadTimeoutMs(15000)
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:141.0) Gecko/20100101 Firefox/141.0")
            .setAllowCrossProtocolRedirects(true)
            .withNetworkRetry()
    )
