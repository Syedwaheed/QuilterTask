package com.example.quiltertask.usecase

import com.example.quiltertask.data.utils.DataError
import com.example.quiltertask.data.utils.Result
import com.example.quiltertask.domain.model.Book
import com.example.quiltertask.domain.repository.BookRepository
import com.example.quiltertask.domain.usecase.GetBookUseCase
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
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }
    @After
    fun tearDown(){
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }
    @Test
    fun `invoke emit success result when repository returns data`(){
        //Arrange
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
        every { bookRepository.getBooks() } returns Single.just(Result.Success(books))

        //Act
        val result = getBookUseCaseTest().blockingGet()

        //Assert
        assertTrue(result is Result.Success)
        assertEquals (books, result.data)

        verify { bookRepository.getBooks()}
    }
    @Test
    fun`invoke emit error when repository returns an error`() {
        //Arrange
        val mockError = DataError.Network.SERVER_ERROR
        every { bookRepository.getBooks() } returns Single.just(Result.Error(mockError))
        //Act
        val result = getBookUseCaseTest().blockingGet()
        //Assert
        assertTrue(result is Result.Error)
        assertEquals(mockError, result.error)
        verify { bookRepository.getBooks() }
    }

    @Test
    fun `invoke propagates exception thrown by repository`() {
        //Arrange
        val throwable = RuntimeException("Oops, something went wrong!")
        every { bookRepository.getBooks() } throws throwable
        //ActAndAssert
        assertFailsWith<RuntimeException> {
            getBookUseCaseTest().blockingGet()
        }
        verify { bookRepository.getBooks() }
    }
}