package com.mashup.gabbangzip.sharedalbum.data.repository

import com.mashup.gabbangzip.sharedalbum.data.base.PicErrorResponse
import com.mashup.gabbangzip.sharedalbum.data.base.PicResponse
import com.mashup.gabbangzip.sharedalbum.data.dto.request.LoginRequest
import com.mashup.gabbangzip.sharedalbum.data.dto.request.TokenRefreshRequest
import com.mashup.gabbangzip.sharedalbum.data.dto.response.LoginResponse
import com.mashup.gabbangzip.sharedalbum.data.dto.response.TokenRefreshResponse
import com.mashup.gabbangzip.sharedalbum.data.service.LoginService
import com.mashup.gabbangzip.sharedalbum.domain.datasource.LocalDataSource
import com.mashup.gabbangzip.sharedalbum.domain.model.LoginParam
import com.mashup.gabbangzip.sharedalbum.domain.model.UserInfoDomainModel
import com.mashup.gabbangzip.sharedalbum.domain.repository.LoginRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class LoginRepositoryImplTest {
    @Mock
    private lateinit var loginService: LoginService

    @Mock
    private lateinit var localDataSource: LocalDataSource
    private lateinit var loginRepository: LoginRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        loginRepository = LoginRepositoryImpl(loginService, localDataSource)
    }

    @Test
    fun `login received valid response`() = runTest {
        // Given
        val param = LoginParam(
            idToken = "token",
            provider = "provider",
            nickname = "user_name",
            profileImage = "image_url",
        )

        val expectedAccessToken = "access_token"
        val expectedRefreshToken = "refresh_token"
        val expectedUserName = "user_name"
        `when`(
            loginService.login(
                LoginRequest(
                    idToken = "token",
                    provider = "provider",
                    nickname = "user_name",
                    profileImage = "image_url",
                ),
            ),
        ).thenReturn(
            PicResponse(
                isSuccess = true,
                data = LoginResponse(
                    userId = 0,
                    nickname = expectedUserName,
                    accessToken = expectedAccessToken,
                    refreshToken = expectedRefreshToken,
                ),
                errorResponse = null,
            ),
        )

        // When
        loginRepository.login(param)

        // Then
        verify(localDataSource).saveToken(expectedAccessToken, expectedRefreshToken)
        verify(localDataSource).saveUserInfo(UserInfoDomainModel(expectedUserName))
    }

    @Test
    fun `login received no data`() = runTest {
        // Given
        val param = LoginParam(
            idToken = "token",
            provider = "provider",
            nickname = "user_name",
            profileImage = "image_url",
        )

        `when`(
            loginService.login(
                LoginRequest(
                    idToken = "token",
                    provider = "provider",
                    nickname = "user_name",
                    profileImage = "image_url",
                ),
            ),
        ).thenReturn(
            PicResponse(
                isSuccess = false,
                data = null,
                errorResponse = PicErrorResponse(
                    code = "ErrorCode",
                    message = "ErrorMessage",
                ),
            ),
        )

        // When & Then
        runCatching {
            loginRepository.login(param)
        }.onFailure {
            Assert.assertThrows(NoSuchElementException::class.java) { throw it }
        }.onSuccess {
            Assert.fail("result is success")
        }
    }

    @Test
    fun `saveToken execute Test`() {
        // Given
        val accessToken = "access_token"
        val refreshToken = "refresh_token"

        // When
        loginRepository.saveToken(accessToken, refreshToken)

        // Then
        verify(localDataSource).saveToken(accessToken, refreshToken)
    }

    @Test
    fun `removeToken execute Test`() {
        // When
        loginRepository.removeToken()

        // Then
        verify(localDataSource).removeToken()
    }

    @Test
    fun `isUserLoggedIn received true when AccessToken is exists`() {
        // Given
        `when`(localDataSource.getAccessToken()).thenReturn("access_token")
        val expected = true

        // When
        val result = loginRepository.isUserLoggedIn()

        // Then
        assertEquals(result, expected)
    }

    @Test
    fun `isUserLoggedIn received false when AccessToken is not exists`() {
        // Given
        `when`(localDataSource.getAccessToken()).thenReturn(null)
        val expected = false

        // When
        val result = loginRepository.isUserLoggedIn()

        // Then
        assertEquals(result, expected)
    }

    @Test
    fun `generateNewAccessToken execute test`() = runTest {
        // Given
        val refreshToken = "refreshToken"

        val expectedAccessToken = "expectedAccessToken"
        val expectedRefreshToken = "expectedRefreshToken"
        `when`(loginService.getNewAccessToken(TokenRefreshRequest(refreshToken))).thenReturn(
            PicResponse(
                isSuccess = true,
                data = TokenRefreshResponse(
                    accessToken = expectedAccessToken,
                    refreshToken = expectedRefreshToken,
                ),
                errorResponse = null,
            ),
        )

        // When & Then
        runCatching {
            loginRepository.generateNewAccessToken(refreshToken)
        }.onFailure {
            fail("generateNewAccessToken returns failed")
        }.onSuccess {
            verify(localDataSource).saveToken(expectedAccessToken, expectedRefreshToken)
        }
    }
}
