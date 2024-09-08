package com.mashup.gabbangzip.sharedalbum.data.common

import org.junit.Assert.assertEquals
import org.junit.Test

class S3ExtTest {
    @Test
    fun `test add S3 url conversion`() {
        // Given
        val url = "pic/8bd0088f-69f9-4379-8c5c-6967b14d9849.png"

        // When
        val s3Url = url.toS3Url()

        // Then
        val expectedS3Url =
            "${Constants.S3_BUCKET_DOMAIN_URL}pic/8bd0088f-69f9-4379-8c5c-6967b14d9849.png"
        assertEquals(expectedS3Url, s3Url)
    }
}