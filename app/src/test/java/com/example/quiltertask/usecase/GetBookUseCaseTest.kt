package com.example.quiltertask.usecase

import com.example.quiltertask.domain.model.Book
import com.example.quiltertask.domain.repository.BookRepository
import com.example.quiltertask.domain.usecase.GetBookUseCase
import com.newapp.composeapplicationstart.data.utils.DataError
import com.newapp.composeapplicationstart.data.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class GetBookUseCaseTest {

    private val bookRepository: BookRepository = mockk()

    private lateinit var getBookUseCaseTest: GetBookUseCase

    @Before
    fun setUp(){
        getBookUseCaseTest = GetBookUseCase(bookRepository)
    }

    @Test
    fun `invoke emit success result when repository returns data`() = runTest{
        val books = listOf(
            Book(
                title = "Book 1",
                author = "Waheed",
                coverID = ""
            ),
            Book(
                title = "Book",
                author = "John",
                coverID = ""
            )
        )
        coEvery { bookRepository.getBooks() } returns flowOf(Result.Success(books))

        val result = getBookUseCaseTest().toList()

        assertTrue(result.first() is Result.Success)
        assertEquals (books,(result.first() as Result.Success).data)

        coVerify { bookRepository.getBooks()}
    }
    @Test
    fun`invoke emit error when repository returns an error`() = runTest {
        val mockError = DataError.Network.SERVER_ERROR
        coEvery { bookRepository.getBooks() } returns flowOf(Result.Error(mockError))
        val result = getBookUseCaseTest().toList()
        assertTrue(result.first() is Result.Error)
        assertEquals(mockError,(result.first() as Result.Error).error)

        coVerify { bookRepository.getBooks() }
    }

    @Test
    fun `invoke propagates exception thrown by repository`() = runTest {
        val throwable = RuntimeException("Oops, something went wrong!")
        coEvery { bookRepository.getBooks() } throws throwable

        assertFailsWith<RuntimeException> {
            getBookUseCaseTest().toList()
        }
        coVerify { bookRepository.getBooks() }


    }
}