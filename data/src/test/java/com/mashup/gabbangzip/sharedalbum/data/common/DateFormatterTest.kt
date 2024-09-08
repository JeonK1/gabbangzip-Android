package com.mashup.gabbangzip.sharedalbum.data.common

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.time.LocalDateTime

class DateFormatterTest {
    @Test
    fun `test valid ISO date time string conversion`() {
        // Given
        val dateTimeString = "2024-09-08T10:15:30"

        // When
        val localDateTime = dateTimeString.toLocalDateTime()

        // Then
        val expectedDateTime = LocalDateTime.of(2024, 9, 8, 10, 15, 30)
        assertEquals(expectedDateTime, localDateTime)
    }

    @Test
    fun `test invalid ISO date time string throws exception`() {
        // Given
        val invalidDateTimeString = "2024-09-08 10:15:30" // Invalid format (missing 'T')

        // When & Then
        assertThrows(java.time.format.DateTimeParseException::class.java) {
            invalidDateTimeString.toLocalDateTime()
        }
    }
}