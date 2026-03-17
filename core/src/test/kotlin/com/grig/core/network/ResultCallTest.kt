package com.grig.core.network

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ResultCallTest {

    private val delegate = mockk<Call<String>>()
    private val resultCall = ResultCall(delegate)

    private fun enqueueAndCapture(): Pair<() -> Result<String>?, Callback<String>> {
        val callbackSlot = slot<Callback<String>>()
        every { delegate.enqueue(capture(callbackSlot)) } answers {}

        var capturedResult: Result<String>? = null
        resultCall.enqueue(object : Callback<Result<String>> {
            override fun onResponse(call: Call<Result<String>>, response: Response<Result<String>>) {
                capturedResult = response.body()
            }

            override fun onFailure(call: Call<Result<String>>, t: Throwable) {
                fail("onFailure should not be called on ResultCall")
            }
        })

        return Pair({ capturedResult }, callbackSlot.captured)
    }

    @Test
    fun `successful response wraps body in Result success`() {
        val (getResult, delegateCallback) = enqueueAndCapture()

        delegateCallback.onResponse(delegate, Response.success("test body"))

        val result = getResult()
        assertNotNull(result)
        assertTrue(result!!.isSuccess)
        assertEquals("test body", result.getOrNull())
    }

    @Test
    fun `error response wraps in Result failure with HttpException`() {
        val (getResult, delegateCallback) = enqueueAndCapture()

        val errorResponse = Response.error<String>(404, "Not Found".toResponseBody(null))
        delegateCallback.onResponse(delegate, errorResponse)

        val result = getResult()
        assertNotNull(result)
        assertTrue(result!!.isFailure)
        assertTrue(result.exceptionOrNull() is HttpException)
    }

    @Test
    fun `null body wraps in Result failure`() {
        val (getResult, delegateCallback) = enqueueAndCapture()

        @Suppress("UNCHECKED_CAST")
        val response = Response.success(null) as Response<String>
        delegateCallback.onResponse(delegate, response)

        val result = getResult()
        assertNotNull(result)
        assertTrue(result!!.isFailure)
        assertTrue(result.exceptionOrNull() is HttpException)
    }

    @Test
    fun `network failure wraps in Result failure with No internet message`() {
        val (getResult, delegateCallback) = enqueueAndCapture()

        delegateCallback.onFailure(delegate, IOException("connection failed"))

        val result = getResult()
        assertNotNull(result)
        assertTrue(result!!.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is RuntimeException)
        assertEquals("No internet connection", exception?.message)
    }
}
