package com.grig.myanimelist.data.model.jikan

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class JikanRelationsParsingTest {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Test
    fun `parses Tenrai relations payload with inline media type and images`() {
        // Trimmed from a real api.tenrai.org/v1/anime/9756/relations response.
        val payload = """
            {"data":[{"relation":"Adaptation","entry":[{"mal_id":8757,
            "type":"manga","name":"Mahou Shoujo Madoka★Magica",
            "url":"https://myanimelist.net/manga/8757","media_type":"Manga",
            "images":{"jpg":{"image_url":"https://cdn.myanimelist.net/images/manga/2/mm.jpg",
            "small_image_url":"https://cdn.myanimelist.net/images/manga/2/mmt.jpg",
            "large_image_url":"https://cdn.myanimelist.net/images/manga/2/mml.jpg"},
            "webp":{"image_url":"https://cdn.myanimelist.net/images/manga/2/mm.webp"}}}]}]}
        """.trimIndent()

        val response = json.decodeFromString<JikanRelationsResponse>(payload)

        val entry = response.data.single().entry.single()
        assertEquals(8757, entry.malId)
        assertEquals("manga", entry.type)
        assertEquals("Manga", entry.mediaType)
        assertEquals("https://cdn.myanimelist.net/images/manga/2/mm.jpg", entry.images?.jpg?.imageUrl)
        assertEquals("https://cdn.myanimelist.net/images/manga/2/mml.jpg", entry.images?.jpg?.largeImageUrl)
    }

    @Test
    fun `parses plain Jikan relations payload without the Tenrai extras`() {
        val payload = """
            {"data":[{"relation":"Adaptation","entry":[{"mal_id":8757,
            "type":"manga","name":"Mahou Shoujo Madoka★Magica",
            "url":"https://myanimelist.net/manga/8757"}]}]}
        """.trimIndent()

        val response = json.decodeFromString<JikanRelationsResponse>(payload)

        val entry = response.data.single().entry.single()
        assertEquals(8757, entry.malId)
        assertNull(entry.mediaType)
        assertNull(entry.images)
    }
}
