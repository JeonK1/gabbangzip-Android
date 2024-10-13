package com.mashup.gabbangzip.sharedalbum.data.repository

import com.mashup.gabbangzip.sharedalbum.data.base.PicErrorResponse
import com.mashup.gabbangzip.sharedalbum.data.base.PicResponse
import com.mashup.gabbangzip.sharedalbum.data.dto.request.notification.toRequestBody
import com.mashup.gabbangzip.sharedalbum.data.dto.response.notification.FcmNotificationResponse
import com.mashup.gabbangzip.sharedalbum.data.dto.response.notification.FcmTokenResponse
import com.mashup.gabbangzip.sharedalbum.data.service.NotificationService
import com.mashup.gabbangzip.sharedalbum.domain.model.notification.FcmNotificationDomainModel
import com.mashup.gabbangzip.sharedalbum.domain.model.notification.FcmNotificationParamDomainModel
import com.mashup.gabbangzip.sharedalbum.domain.model.notification.FcmTokenDomainModel
import com.mashup.gabbangzip.sharedalbum.domain.model.notification.FcmTokenParamDomainModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class NotificationRepositoryImplTest {

    @Mock
    private lateinit var notificationService: NotificationService
    private lateinit var notificationRepository: NotificationRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        notificationRepository = NotificationRepositoryImpl(notificationService)
    }

    @Test
    fun `registerFcmToken received valid response`() = runTest {
        // Given
        val param = FcmTokenParamDomainModel("sample_token")
        `when`(notificationService.registerToken(param.toRequestBody()))
            .thenReturn(
                PicResponse(
                    isSuccess = true,
                    data = FcmTokenResponse("registered_token"),
                    errorResponse = null,
                ),
            )
        val expectedDomainModel = FcmTokenDomainModel("registered_token")

        // When
        val result = notificationRepository.registerFcmToken(param)

        // Then
        assertEquals(expectedDomainModel, result)
    }

    @Test
    fun `registerFcmToken received no data`() = runTest {
        // Given
        val param = FcmTokenParamDomainModel("sample_token")
        `when`(notificationService.registerToken(param.toRequestBody()))
            .thenReturn(
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
            notificationRepository.registerFcmToken(param)
        }.onFailure {
            assertThrows(NoSuchElementException::class.java) { throw it }
        }.onSuccess {
            Assert.fail("result is success")
        }
    }

    @Test
    fun `sendFcmNotification received valid response`() = runTest {
        // Given
        val param = FcmNotificationParamDomainModel(0)
        `when`(notificationService.sendFcmNotification(param.toRequestBody()))
            .thenReturn(
                PicResponse(
                    isSuccess = true,
                    data = FcmNotificationResponse(123),
                    errorResponse = null,
                ),
            )
        val expectedDomainModel = FcmNotificationDomainModel(123)

        // When
        val result = notificationRepository.sendFcmNotification(param)

        // Then
        assertEquals(expectedDomainModel, result)
    }

    @Test
    fun `sendFcmNotification received no data`() = runTest {
        // Given
        val param = FcmNotificationParamDomainModel(0)
        `when`(notificationService.sendFcmNotification(param.toRequestBody()))
            .thenReturn(
                PicResponse(
                    isSuccess = false,
                    data = null,
                    errorResponse = null,
                ),
            )

        // When & Then
        runCatching {
            notificationRepository.sendFcmNotification(param)
        }.onFailure {
            assertThrows(NoSuchElementException::class.java) { throw it }
        }.onSuccess {
            Assert.fail("result is success")
        }
    }
}
