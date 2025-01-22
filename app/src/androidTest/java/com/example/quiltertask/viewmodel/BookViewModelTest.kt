package com.example.quiltertask.viewmodel

import android.content.res.Resources
import com.example.quiltertask.R
import com.example.quiltertask.domain.model.Book
import com.example.quiltertask.domain.repository.ErrorMapper
import com.example.quiltertask.domain.usecase.GetBookUseCase
import com.example.quiltertask.presentation.state.BookUiState
import com.example.quiltertask.presentation.utils.UIText
import com.example.quiltertask.presentation.viewmodel.BookViewModel
import com.newapp.composeapplicationstart.data.utils.DataError
import com.newapp.composeapplicationstart.data.utils.Result
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class BookViewModelTest {
    private val getBookUseCase: GetBookUseCase = mockk()
    private val errorMapper: ErrorMapper = mockk()
    private lateinit var bookViewModel: BookViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        RxJavaPlugins.reset()
    }

    @Test
    fun fetchBooksWhenReturnSuccess() = runTest {
        // Arrange
        val mockBooks = listOf(Book("1", "Title", "Author"))

        coEvery { getBookUseCase.invoke() } returns flow { emit(Result.Success(mockBooks)) }
        // Act
        bookViewModel = BookViewModel(getBookUseCase, errorMapper)
        bookViewModel.fetchBooks()
        advanceUntilIdle()
        // Assert
        val result = bookViewModel.bookUiState.first()
        assertEquals(BookUiState.Success(mockBooks), result)
    }

    @Test
    fun returnErrorWhenFetchBooksThrowError() = runTest {
        // Arrange
        val errorText = Throwable("Check your internet connection")
        val mappedError = DataError.Network.NO_INTERNET
        val uiText = UIText.StringResource(R.string.no_internet)


        coEvery { getBookUseCase.invoke() } returns flow { throw errorText } // Simulate error in use case
        every { errorMapper.mapError(errorText) } returns mappedError // Map Throwable to DataError
        // Act
        bookViewModel = BookViewModel(getBookUseCase, errorMapper)
        bookViewModel.fetchBooks()
        advanceUntilIdle()

        // Assert
        val result = bookViewModel.bookUiState.value as BookUiState.Error
        assertEquals(uiText, result.message)
    }
}