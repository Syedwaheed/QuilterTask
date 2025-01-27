package com.example.quiltertask.repository

import com.example.quiltertask.data.datasource.model.response.ApiResponseDTO
import com.example.quiltertask.data.datasource.remote.BookApiService
import com.example.quiltertask.data.mapper.BookMapper
import com.example.quiltertask.data.repository.BookRepositoryImpl
import com.example.quiltertask.data.utils.DataError
import com.example.quiltertask.data.utils.Result
import com.example.quiltertask.domain.model.Book
import com.example.quiltertask.domain.repository.ErrorMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class BookRepositoryImpTest {

    private lateinit var repository: BookRepositoryImpl
    private val apiService: BookApiService = mockk()
    private val bookMapper: BookMapper = mockk()
    private val errorMapper: ErrorMapper = mockk()


    @Before
    fun setUp() {
        repository = BookRepositoryImpl(apiService, bookMapper,errorMapper)
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler{Schedulers.trampoline()}
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    @Test
    fun `getBooks return success when API call is successful`() {
        //Arrange
        val mockWork = ApiResponseDTO.ReadingLogEntry.Work(
            title = "Mock Title",
            authorNames = listOf("Mock Author"),
            coverId = 0,
        )
        val mockReadingLogEntry = ApiResponseDTO.ReadingLogEntry(
            loggedEdition = null,
            loggedDate = null,
            work = mockWork
        )
        val mockApiResponse = ApiResponseDTO(
            numFound = null,
            page = null,
            readingLogEntries = listOf(mockReadingLogEntry)
        )
        every { apiService.getBooks() } returns Single.just(mockApiResponse)

        val mockBook = Book(
            title = "",
            author = "Mock Author",
            coverID = ""
        )
        every { bookMapper.mapApiResponseDTO(mockWork) } returns mockBook

        //Act
        val result = repository.getBooks().blockingGet()

        //Assert
        assertTrue(result is Result.Success)
        val data = result.data
        assertNotNull(data)
        assertEquals(1, data.size)
        assertEquals(mockBook, data.first())
    }


    @Test
    fun `getBooks return error when Api call throws exception`(){
        //Arrange
        val throwable = RuntimeException("Oops, something went wrong. Please try again.")
        every { apiService.getBooks() } returns Single.error(throwable)

        val mockError = DataError.Network.NO_INTERNET
        every { errorMapper.mapError(throwable) } returns mockError

        //Act
        val result = repository.getBooks().blockingGet()

        //Assert
        assertTrue(result is Result.Error)
        val error = result.error
        assertEquals(mockError, error)

        verify { apiService.getBooks() }
        verify { errorMapper.mapError(throwable) }
    }

    @Test
    fun`getBooks return empty list when Api response has no readingLogEntries`() {
        //Arrange
        val apiResponse = ApiResponseDTO(
            numFound = null,
            page = null,
            readingLogEntries = null
        )
        every { apiService.getBooks() } returns Single.just(apiResponse)

        //Act
        val result = repository.getBooks().blockingGet()

        //Assert
        assertTrue(result is Result.Success)
        val data = result.data
        assertTrue(data.isEmpty())

    }

}