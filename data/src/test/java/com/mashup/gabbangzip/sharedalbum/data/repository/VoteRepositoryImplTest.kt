package com.mashup.gabbangzip.sharedalbum.data.repository

import com.mashup.gabbangzip.sharedalbum.data.base.PicErrorResponse
import com.mashup.gabbangzip.sharedalbum.data.base.PicResponse
import com.mashup.gabbangzip.sharedalbum.data.common.toS3Url
import com.mashup.gabbangzip.sharedalbum.data.dto.request.vote.toRequestBody
import com.mashup.gabbangzip.sharedalbum.data.dto.response.vote.VotePhotoOptionResponse
import com.mashup.gabbangzip.sharedalbum.data.dto.response.vote.VotePhotoResponse
import com.mashup.gabbangzip.sharedalbum.data.dto.response.vote.VoteResultResponse
import com.mashup.gabbangzip.sharedalbum.data.service.VoteService
import com.mashup.gabbangzip.sharedalbum.domain.datasource.LocalDataSource
import com.mashup.gabbangzip.sharedalbum.domain.model.vote.VotePhotoDomainModel
import com.mashup.gabbangzip.sharedalbum.domain.model.vote.VoteResultDomainModel
import com.mashup.gabbangzip.sharedalbum.domain.model.vote.VoteResultParam
import com.mashup.gabbangzip.sharedalbum.domain.repository.VoteRepository
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

class VoteRepositoryImplTest {
    @Mock
    private lateinit var voteService: VoteService

    @Mock
    private lateinit var localDataSource: LocalDataSource
    private lateinit var voteRepository: VoteRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        voteRepository = VoteRepositoryImpl(voteService, localDataSource)
    }

    @Test
    fun `requestVoteResult received valid response`() = runTest {
        // Given
        val param = VoteResultParam(0, listOf(0, 1, 2, 3))
        `when`(voteService.requestVoteResult(param.toRequestBody())).thenReturn(
            PicResponse(
                isSuccess = true,
                data = VoteResultResponse(
                    eventId = 0,
                    randomImageUrl = "image_url",
                    groupKeyword = "HOBBY",
                ),
                errorResponse = null,
            ),
        )
        val expected = VoteResultDomainModel(
            eventId = 0,
            randomImageUrl = "image_url".toS3Url(),
            groupKeyword = "HOBBY",
        )

        // When
        val result = voteRepository.requestVoteResult(param)

        // Then
        assertEquals(result, expected)
    }

    @Test
    fun `requestVoteResult received no data`() = runTest {
        // Given
        val param = VoteResultParam(0, listOf(0, 1, 2, 3))
        `when`(voteService.requestVoteResult(param.toRequestBody())).thenReturn(
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
            voteRepository.requestVoteResult(param)
        }.onFailure {
            Assert.assertThrows(NoSuchElementException::class.java) { throw it }
        }.onSuccess {
            fail("result is success")
        }
    }

    @Test
    fun `getVotePhotoList received valid response`() = runTest {
        // Given
        val param = 0L
        `when`(voteService.getVotePhotoList(param)).thenReturn(
            PicResponse(
                isSuccess = true,
                data = VotePhotoResponse(
                    options = listOf(
                        VotePhotoOptionResponse(
                            imageUrl = "image_url_1",
                            optionId = 0,
                        ),
                        VotePhotoOptionResponse(
                            imageUrl = "image_url_2",
                            optionId = 1,
                        ),
                    ),
                ),
                errorResponse = null,
            ),
        )
        val expected = listOf(
            VotePhotoDomainModel(
                id = 0,
                imageUrl = "image_url_1".toS3Url(),
            ),
            VotePhotoDomainModel(
                id = 1,
                imageUrl = "image_url_2".toS3Url(),
            ),
        )

        // When
        val result = voteRepository.getVotePhotoList(param)

        // Then
        assertEquals(result, expected)
    }

    @Test
    fun `getVotePhotoList received no data`() = runTest {
        // Given
        val param = 0L
        `when`(voteService.getVotePhotoList(param)).thenReturn(
            PicResponse(
                isSuccess = true,
                data = null,
                errorResponse = PicErrorResponse(
                    code = "ErrorCode",
                    message = "ErrorMessage",
                ),
            ),
        )

        // When & Then
        runCatching {
            voteRepository.getVotePhotoList(param)
        }.onFailure {
            Assert.assertThrows(NoSuchElementException::class.java) { throw it }
        }.onSuccess {
            fail("result is success")
        }
    }

    @Test
    fun `getVoteFirstVisit received valid response`() {
        // Given
        `when`(localDataSource.getVoteFirstVisit()).thenReturn(false)
        val expected = false

        // When
        val result = voteRepository.getVoteFirstVisit()

        // Then
        verify(localDataSource).getVoteFirstVisit()
        assertEquals(result, expected)
    }

    @Test
    fun `saveVoteFirstVisit received valid response`() {
        // Given
        val param = true

        // When
        voteRepository.saveVoteFirstVisit(param)

        // Then
        verify(localDataSource).saveVoteFirstVisit(param)
    }
}
