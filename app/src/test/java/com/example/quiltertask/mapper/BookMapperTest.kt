package com.example.quiltertask.mapper

import com.example.quiltertask.data.datasource.model.response.ApiResponseDTO
import com.example.quiltertask.data.mapper.BookMapper
import com.example.quiltertask.data.utils.UrlConstant
import org.junit.Assert.assertEquals
import org.junit.Test

class BookMapperTest {
    private val mapper = BookMapper()

    @Test
    fun `mapApiResponseDTO maps correctly`(){
        val work = ApiResponseDTO.ReadingLogEntry.Work(
            title = "The Food Lab",
            authorNames = listOf("J. Kenji López-Alt"),
            coverId = 8314250
        )
        val book = mapper.mapApiResponseDTO(work)
        val expectedCoverURL = UrlConstant.getCoverUrl(8314250)

        assertEquals("The Food Lab", book.title)
        assertEquals("J. Kenji López-Alt",book.author)
        assertEquals(expectedCoverURL,book.coverID)
    }

    @Test
    fun `mapApiResponseDTO handles null coverID`(){
        val work = ApiResponseDTO.ReadingLogEntry.Work(
            title = "The Food Lab",
            authorNames = listOf("J. Kenji López-Alt"),
            coverId = null
        )
        val book = mapper.mapApiResponseDTO(work)

        assertEquals("The Food Lab",book.title)
        assertEquals("J. Kenji López-Alt",book.author)
        assertEquals("",book.coverID)
    }
}