package com.mashup.gabbangzip.sharedalbum.data.repository

import com.mashup.gabbangzip.sharedalbum.data.service.UserService
import com.mashup.gabbangzip.sharedalbum.domain.datasource.LocalDataSource
import com.mashup.gabbangzip.sharedalbum.domain.model.UserInfoDomainModel
import com.mashup.gabbangzip.sharedalbum.domain.repository.UserRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class UserRepositoryImplTest {

    @Mock
    private lateinit var userService: UserService
    @Mock
    private lateinit var localDataSource: LocalDataSource
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        userRepository = UserRepositoryImpl(userService, localDataSource)
    }

    @Test
    fun `loadUserInfo received valid response`() {
        // Given
        `when`(localDataSource.loadUserInfo()).thenReturn(
            UserInfoDomainModel("user_name")
        )
        val expected = UserInfoDomainModel("user_name")

        // When
        val result = userRepository.loadUserInfo()

        // Then
        assertEquals(result, expected)
    }

    @Test
    fun `deleteUser check execute functions`() = runTest {
        // When
        userRepository.deleteUser()

        // Then
        verify(userService).deleteUser()
        verify(localDataSource).removeUserInfo()
    }
}
