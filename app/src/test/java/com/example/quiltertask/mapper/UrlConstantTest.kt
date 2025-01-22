package com.example.quiltertask.mapper

import com.example.quiltertask.data.utils.UrlConstant
import org.junit.Assert.assertEquals
import org.junit.Test

class UrlConstantTest{

    @Test
    fun `getCoverUrl returns correct URl with default size`(){
        val url = UrlConstant.getCoverUrl(8314250)
        assertEquals("https://covers.openlibrary.org/b/id/8314250-M.jpg",url)
    }

    @Test
    fun `getCoverUrl returns correct URL with custom size`(){
        val url = UrlConstant.getCoverUrl(8314250, "L")
        assertEquals("https://covers.openlibrary.org/b/id/8314250-L.jpg",url)
    }
}