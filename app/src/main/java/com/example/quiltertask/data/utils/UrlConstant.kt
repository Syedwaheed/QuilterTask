package com.example.quiltertask.data.utils

object UrlConstant {
    private const val BASE_COVER_URL = "https://covers.openlibrary.org/b/id/"

    fun getCoverUrl(coverId: Int, size: String ="M"): String{
        return "$BASE_COVER_URL$coverId-$size.jpg"
    }
}