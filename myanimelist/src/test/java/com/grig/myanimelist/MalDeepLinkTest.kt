package com.grig.myanimelist

import org.junit.Assert.assertEquals
import org.junit.Test

class MalDeepLinkTest {

    @Test
    fun `anime link with slug and query opens anime detail`() {
        assertEquals(
            listOf(MalRoute.AnimeDetail(65077)),
            parseMalDeepLink("https://myanimelist.net/anime/65077/Mushoku_Tensei_III__Isekai_Ittara_Honki_Dasu_Part_2?_user=6875424")
        )
    }

    @Test
    fun `www host, http and trailing slash are accepted`() {
        assertEquals(listOf(MalRoute.AnimeDetail(1)), parseMalDeepLink("http://www.myanimelist.net/anime/1/"))
    }

    @Test
    fun `manga link opens manga detail`() {
        assertEquals(listOf(MalRoute.MangaDetail(2)), parseMalDeepLink("https://myanimelist.net/manga/2/Berserk"))
    }

    @Test
    fun `reviews and characters pages open on top of their detail`() {
        assertEquals(
            listOf(MalRoute.AnimeDetail(5114), MalRoute.Reviews(5114, "anime")),
            parseMalDeepLink("https://myanimelist.net/anime/5114/Fullmetal_Alchemist__Brotherhood/reviews")
        )
        assertEquals(
            listOf(MalRoute.MangaDetail(2), MalRoute.Characters(2, "manga")),
            parseMalDeepLink("https://myanimelist.net/manga/2/Berserk/characters")
        )
    }

    @Test
    fun `other media sub-pages fall back to the detail`() {
        assertEquals(
            listOf(MalRoute.AnimeDetail(5114)),
            parseMalDeepLink("https://myanimelist.net/anime/5114/Fullmetal_Alchemist__Brotherhood/stats")
        )
    }

    @Test
    fun `character, people and producer links open their screens`() {
        assertEquals(
            listOf(MalRoute.CharacterDetail(111245)),
            parseMalDeepLink("https://myanimelist.net/character/111245/Rudeus_Greyrat")
        )
        assertEquals(
            listOf(MalRoute.PersonDetail(11784)),
            parseMalDeepLink("https://myanimelist.net/people/11784/Yumi_Uchiyama")
        )
        assertEquals(
            listOf(MalRoute.StudioDetail(1)),
            parseMalDeepLink("https://myanimelist.net/anime/producer/1/Studio_Pierrot")
        )
    }

    @Test
    fun `pages without an in-app screen are ignored`() {
        listOf(
            "https://myanimelist.net/anime/season",
            "https://myanimelist.net/anime/genre/1/Action",
            "https://myanimelist.net/v1/oauth2/authorize?response_type=code",
            "https://myanimelist.net/profile/someone",
            "https://api.myanimelist.net/v2/anime/1",
            "https://example.com/anime/1",
            "app://grigmal.auth?code=abc",
            "not a url",
            null
        ).forEach { url ->
            assertEquals(url, emptyList<MalRoute>(), parseMalDeepLink(url))
        }
    }
}
