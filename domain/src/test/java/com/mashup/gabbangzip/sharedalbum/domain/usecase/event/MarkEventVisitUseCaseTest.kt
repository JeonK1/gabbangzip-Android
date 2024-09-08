package com.mashup.gabbangzip.sharedalbum.domain.usecase.event

import com.mashup.gabbangzip.sharedalbum.domain.model.event.EventVisitDomainModel
import com.mashup.gabbangzip.sharedalbum.domain.model.event.EventVisitParamDomainModel
import com.mashup.gabbangzip.sharedalbum.domain.repository.EventRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MarkEventVisitUseCaseTest {

    @RelaxedMockK
    private lateinit var mockEventRepository: EventRepository

    private lateinit var useCase: MarkEventVisitUseCase

    @Before
    fun setUp() {
        mockEventRepository = mockk(relaxed = true)
        useCase = MarkEventVisitUseCase(mockEventRepository)
    }

    @Test
    fun `should return success result when event visit is marked`() = runBlocking {
        // Arrange
        val eventId = 123L
        val expectedDomainModel = EventVisitDomainModel(true) // 예시로, 실제 도메인 모델로 교체
        coEvery { mockEventRepository.markEventVisit(any()) } returns expectedDomainModel

        // Act
        val result = useCase(eventId)

        // Assert
        assertEquals(Result.success(expectedDomainModel), result)
        coVerify { mockEventRepository.markEventVisit(EventVisitParamDomainModel(eventId)) }
    }

    @Test
    fun `should return failure result when an exception occurs`() = runBlocking {
        // Arrange
        val eventId = 123L
        val exception = RuntimeException("Error occurred")
        coEvery { mockEventRepository.markEventVisit(any()) } throws exception

        // Act
        val result = useCase(eventId)

        // Assert
        assertEquals(Result.failure<EventVisitDomainModel>(exception), result)
        coVerify { mockEventRepository.markEventVisit(EventVisitParamDomainModel(eventId)) }
    }
}