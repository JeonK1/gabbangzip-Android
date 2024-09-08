package com.mashup.gabbangzip.sharedalbum.data.datasource

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import com.mashup.gabbangzip.sharedalbum.domain.datasource.LocalDataSource
import com.mashup.gabbangzip.sharedalbum.domain.model.UserInfoDomainModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LocalDataSourceImplTest {
    private lateinit var localDataSource: LocalDataSource
    private lateinit var sharedPreferences: SharedPreferences

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        localDataSource = LocalDataSourceImpl(context)

        // 실제로 암호화된 SharedPreferences를 사용하지 않기 위해 일반 SharedPreferences를 사용
        sharedPreferences = context.getSharedPreferences("test_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().commit() // 초기화
    }

    @Test
    fun testSaveToken() {
        // Given
        val accessToken = "accessToken"
        val refreshToken = "refreshToken"

        // When
        localDataSource.saveToken(accessToken, refreshToken)

        // Then
        assertEquals(accessToken, localDataSource.getAccessToken())
        assertEquals(refreshToken, localDataSource.getRefreshToken())
    }

    @Test
    fun testRemoveToken() {
        // Given
        val accessToken = "accessToken"
        val refreshToken = "refreshToken"
        localDataSource.saveToken(accessToken, refreshToken)

        // When
        localDataSource.removeToken()

        // Then
        assertEquals(null, localDataSource.getAccessToken())
        assertEquals(null, localDataSource.getRefreshToken())
    }

    @Test
    fun testSaveUserInfo() {
        // Given
        val userInfo = UserInfoDomainModel("user")

        // When
        localDataSource.saveUserInfo(userInfo)

        // Then
        assertEquals(userInfo, localDataSource.loadUserInfo())
    }

    @Test
    fun testRemoveUserInfo() {
        // Given
        val userInfo = UserInfoDomainModel("user")
        localDataSource.saveUserInfo(userInfo)

        // When
        localDataSource.removeUserInfo()

        // Then
        val expectedUserInfo = UserInfoDomainModel("")
        assertEquals(expectedUserInfo, localDataSource.loadUserInfo())
    }

    @Test
    fun testRemoveAll() {
        // Given
        val accessToken = "accessToken"
        val refreshToken = "refreshToken"
        localDataSource.saveToken(accessToken, refreshToken)
        val userInfo = UserInfoDomainModel("user")
        localDataSource.saveUserInfo(userInfo)

        // When
        localDataSource.removeAll()

        // Then
        assertEquals(null, localDataSource.getAccessToken())
        assertEquals(null, localDataSource.getRefreshToken())
        val expectedUserInfo = UserInfoDomainModel("")
        assertEquals(expectedUserInfo, localDataSource.loadUserInfo())
    }

    @Test
    fun testSaveVoteFirstVisit_isFirstVisit_True() {
        // Given
        val isFirstVisit = true

        // When
        localDataSource.saveVoteFirstVisit(isFirstVisit)

        // Then
        assertEquals(isFirstVisit, localDataSource.getVoteFirstVisit())
    }

    @Test
    fun testSaveVoteFirstVisit_isFirstVisit_False() {
        // Given
        val isFirstVisit = false

        // When
        localDataSource.saveVoteFirstVisit(isFirstVisit)

        // Then
        assertEquals(isFirstVisit, localDataSource.getVoteFirstVisit())
    }
}