package com.example.quiltertask.data.datasource.model.response


import com.google.gson.annotations.SerializedName

data class ApiResponseDTO(
    @SerializedName("numFound")
    val numFound: Int?,
    @SerializedName("page")
    val page: Int?,
    @SerializedName("reading_log_entries")
    val readingLogEntries: List<ReadingLogEntry?>?
) {
    data class ReadingLogEntry(
        @SerializedName("logged_date")
        val loggedDate: String?,
        @SerializedName("logged_edition")
        val loggedEdition: String?,
        @SerializedName("work")
        val work: Work?
    ) {
        data class Work(
            @SerializedName("author_keys")
            val authorKeys: List<String?>? = emptyList(),
            @SerializedName("author_names")
            val authorNames: List<String?>?,
            @SerializedName("cover_edition_key")
            val coverEditionKey: String? = "",
            @SerializedName("cover_id")
            val coverId: Int?,
            @SerializedName("edition_key")
            val editionKey: List<String?>? = emptyList(),
            @SerializedName("first_publish_year")
            val firstPublishYear: Int? = 0,
            @SerializedName("key")
            val key: String? = "",
            @SerializedName("lending_edition_s")
            val lendingEditionS: String? = "",
            @SerializedName("title")
            val title: String?
        )
    }
}