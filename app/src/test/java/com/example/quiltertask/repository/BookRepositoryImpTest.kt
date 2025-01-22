package com.example.quiltertask.repository

import com.example.quiltertask.data.datasource.model.response.ApiResponseDTO
import com.example.quiltertask.data.datasource.remote.BookApiService
import com.example.quiltertask.data.mapper.BookMapper
import com.example.quiltertask.data.repository.BookRepositoryImpl
import com.example.quiltertask.domain.model.Book
import com.example.quiltertask.domain.repository.ErrorMapper
import com.example.quiltertask.data.utils.DataError
import com.example.quiltertask.data.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
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

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = BookRepositoryImpl(apiService, bookMapper,errorMapper)
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        RxJavaPlugins.reset()
    }

    @Test
    fun `getBooks return success when API call is successful`() = runTest {
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
        coEvery { apiService.getBooks() } returns Observable.just(mockApiResponse)

        val mockBook = Book(
            title = "",
            author = "Mock Author",
            coverID = ""
        )
        every { bookMapper.mapApiResponseDTO(mockWork) } returns mockBook

        val result = repository.getBooks().first()
        assertTrue(result is Result.Success)
        val data = result.data
        assertNotNull(data)
        assertEquals(1, data.size)
        assertEquals(mockBook, data.first())
    }


    @Test
    fun `getBooks return error when Api call throws exception`() = runTest {
        val throwable = RuntimeException("Oops, something went wrong. Please try again.")
        coEvery { apiService.getBooks() } returns Observable.error(throwable)

        val mockError = DataError.Network.NO_INTERNET
        every { errorMapper.mapError(throwable) } returns mockError

        val result = repository.getBooks().first()
        assertTrue(result is Result.Error)
        val error = result.error
        assertEquals(mockError, error)

        coVerify { apiService.getBooks() }
        verify { errorMapper.mapError(throwable) }
    }

    @Test
    fun`getBooks return empty list when Api response has no readingLogEntries`() = runTest{
        val apiResponse = ApiResponseDTO(
            numFound = null,
            page = null,
            readingLogEntries = null
        )
        coEvery { apiService.getBooks() } returns Observable.just(apiResponse)

        val result = repository.getBooks().first()

        assertTrue(result is Result.Success)
        val data = result.data
        assertTrue(data.isEmpty())

    }

}