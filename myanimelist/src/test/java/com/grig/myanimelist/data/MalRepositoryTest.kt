package com.grig.myanimelist.data

import android.net.Uri
import com.grig.myanimelist.data.model.MalTokenResponse
import com.grig.myanimelist.data.model.MalUser
import com.grig.myanimelist.data.model.MalUserState
import com.grig.myanimelist.tools.generateCodeVerifier
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MalRepositoryTest {

    private val malAuthService: MalAuthService = mockk()
    private val malService: MalService = mockk()
    private val userManager: UserManager = mockk(relaxed = true)

    private lateinit var repository: MalRepository

    @Before
    fun setUp() {
        mockkStatic("com.grig.myanimelist.tools.NetworkUtilsKt")
        every { generateCodeVerifier() } returns "test-code-verifier"
        every { userManager.userFlow } returns flowOf(MalUserState.Unauthorized)
        repository = MalRepository(malAuthService, malService, userManager)
    }

    @After
    fun tearDown() {
        unmockkStatic("com.grig.myanimelist.tools.NetworkUtilsKt")
    }

    @Test
    fun `loginUri contains required OAuth params`() {
        val uri = repository.loginUri()

        assertTrue(uri.startsWith("https://myanimelist.net/v1/oauth2/authorize?"))
        assertTrue(uri.contains("response_type=code"))
        assertTrue(uri.contains("client_id="))
        assertTrue(uri.contains("code_challenge="))
        assertTrue(uri.contains("code_challenge_method=plain"))
    }

    @Test
    fun `getAuthorizationCode extracts code from valid URI`() {
        val uri = mockk<Uri>()
        every { uri.getQueryParameter("code") } returns "test-auth-code"

        val result = repository.getAuthorizationCode(uri)

        assertEquals("test-auth-code", result)
    }

    @Test
    fun `getAuthorizationCode returns null for null URI`() {
        val result = repository.getAuthorizationCode(null)

        assertNull(result)
    }

    @Test
    fun `getAuthorizationCode returns null when code param is missing`() {
        val uri = mockk<Uri>()
        every { uri.getQueryParameter("code") } returns null

        val result = repository.getAuthorizationCode(uri)

        assertNull(result)
    }

    @Test
    fun `auth saves tokens and user on success`() = runTest {
        val tokenResponse = MalTokenResponse(
            tokenType = "Bearer",
            expiresIn = 3600,
            accessToken = "access-token",
            refreshToken = "refresh-token"
        )
        val user = MalUser(id = 1, name = "TestUser", joinedAt = "2020-01-01")

        coEvery { malAuthService.getToken(any(), any(), any(), any()) } returns Result.success(tokenResponse)
        coEvery { malService.getUser() } returns Result.success(user)

        repository.auth("auth-code")

        coVerify { userManager.saveTokens("access-token", "refresh-token") }
        coVerify { userManager.saveUser(user) }
    }

    @Test
    fun `auth handles token failure gracefully`() = runTest {
        coEvery { malAuthService.getToken(any(), any(), any(), any()) } returns
            Result.failure(RuntimeException("token error"))
        coEvery { malService.getUser() } returns
            Result.failure(RuntimeException("user error"))

        repository.auth("auth-code")

        coVerify(exactly = 0) { userManager.saveTokens(any(), any()) }
    }

    @Test
    fun `logout delegates to userManager`() = runTest {
        repository.logout()

        coVerify { userManager.logout() }
    }
}
