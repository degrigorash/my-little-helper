package com.grig.danish.data

import android.content.Context
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File

class DanishRepositoryTest {

    @MockK
    private lateinit var service: DanishService

    @MockK
    private lateinit var context: Context

    private lateinit var repository: DanishRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        val tempDir = File(System.getProperty("java.io.tmpdir"), "danish_test_${System.nanoTime()}")
        tempDir.mkdirs()
        every { context.filesDir } returns tempDir
        repository = DanishRepository(service, context)
    }

    @Test
    fun `findLink extracts audio URL from HTML`() {
        val html = """<div class="audio id"><source src="https://static.ordnet.dk/mp3/11000/11000123.mp3" type="audio/mpeg"></div>"""

        val method = DanishRepository::class.java.getDeclaredMethod("findLink", String::class.java)
        method.isAccessible = true
        val result = method.invoke(repository, html) as String

        assertEquals("https://static.ordnet.dk/mp3/11000/11000123.mp3", result)
    }

    @Test
    fun `findLink handles different URL patterns`() {
        val html = """some content audio id more text https://audio.example.com/sounds/word.mp3 trailing"""

        val method = DanishRepository::class.java.getDeclaredMethod("findLink", String::class.java)
        method.isAccessible = true
        val result = method.invoke(repository, html) as String

        assertTrue(result.startsWith("https://"))
        assertTrue(result.endsWith(".mp3"))
        assertEquals("https://audio.example.com/sounds/word.mp3", result)
    }
}
