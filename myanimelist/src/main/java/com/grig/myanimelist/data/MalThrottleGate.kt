package com.grig.myanimelist.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
import kotlin.time.Duration

/**
 * Coordinates a burst of MAL requests around MAL's silent throttling.
 *
 * After a few hundred rapid requests MAL stops answering for ~60–90s: no 429, no error,
 * requests just hang until the socket read timeout. Waiting out the client's 30s timeout
 * per request freezes the fan-out, so each request here gets a short [requestTimeout].
 * The first timeout marks MAL as throttled: callers then queue behind a single probe that
 * waits [pause] and retries its request, and everyone resumes once a probe succeeds.
 *
 * After [maxFailedProbes] consecutive failed probes the gate gives up and every remaining
 * request returns null, so a network that is simply too slow can't loop forever.
 *
 * [onThrottledChange] is invoked with true when requests get paused and false once they
 * resume (or the gate gives up), so the UI can explain the wait.
 */
internal class MalThrottleGate(
    private val requestTimeout: Duration,
    private val pause: Duration,
    private val maxFailedProbes: Int,
    private val onThrottledChange: (throttled: Boolean) -> Unit = {}
) {
    private val probeMutex = Mutex()
    @Volatile private var throttled = false
    @Volatile private var gaveUp = false
    private var failedProbes = 0 // guarded by probeMutex

    /** Runs [request], retrying it through throttling. Returns null only once the gate gave up. */
    suspend fun <T : Any> run(request: suspend () -> T): T? {
        while (!gaveUp) {
            if (!throttled) {
                withTimeoutOrNull(requestTimeout) { request() }?.let { return it }
                if (!throttled) {
                    Timber.w("MAL stopped responding, pausing requests")
                    throttled = true
                    onThrottledChange(true)
                }
                continue
            }
            probeMutex.withLock {
                if (throttled && !gaveUp) {
                    delay(pause)
                    withTimeoutOrNull(requestTimeout) { request() }?.let {
                        throttled = false
                        failedProbes = 0
                        onThrottledChange(false)
                        return it
                    }
                    if (++failedProbes >= maxFailedProbes) {
                        Timber.w("MAL still not responding after $failedProbes probes, giving up")
                        gaveUp = true
                        onThrottledChange(false)
                    }
                }
            }
        }
        return null
    }
}
