package com.example.quiltertask.ui

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.quiltertask.R
import com.example.quiltertask.data.utils.DataError
import com.example.quiltertask.data.utils.Result
import com.example.quiltertask.domain.model.Book
import com.example.quiltertask.domain.repository.ErrorMapper
import com.example.quiltertask.domain.usecase.GetBookUseCase
import com.example.quiltertask.presentation.state.BookUiState
import com.example.quiltertask.presentation.utils.UIText
import com.example.quiltertask.presentation.viewmodel.BookViewModel
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookViewModelTest {
    private val getBookUseCase: GetBookUseCase = mockk()
    private val errorMapper: ErrorMapper = mockk()
    private lateinit var bookViewModel: BookViewModel

    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    @Test
    fun testFetchBook_showBookUILoadingState() {
        //Arrange
        every { getBookUseCase() } returns Single.never()

        //Act
        bookViewModel = BookViewModel(getBookUseCase, errorMapper)

        //Assert
        val result = bookViewModel.bookUiState.value
        assertTrue(result is BookUiState.Loading)
    }


    @Test
    fun testFetchBook_showBookUISuccessState() {
        //Arrange
        val mockBooks = listOf(
            Book(
                title = "Title",
                author = "waheed",
                coverID = ""
            ),
            Book(
                title = "Title2",
                author = "John",
                coverID = ""
            )
        )
        every { getBookUseCase() } returns Single.just(Result.Success(mockBooks))
        //Act
        bookViewModel = BookViewModel(getBookUseCase,errorMapper)
        bookViewModel.fetchBooks()
        //Assert
        val result = bookViewModel.bookUiState.value
        assertEquals(BookUiState.Success(mockBooks),result)

    }

    @Test
    fun testFetchBook_showBookUIErrorState() {
        // Arrange
        val errorText = Throwable("Check your internet connection.")
        val mappedError = DataError.Network.NO_INTERNET
        val uiText = UIText.StringResource(R.string.no_internet)

        every { getBookUseCase() } returns Single.error(errorText)
        every { errorMapper.mapError(errorText) } returns mappedError

        //Act
        bookViewModel = BookViewModel(getBookUseCase,errorMapper)
        bookViewModel.fetchBooks()

        //Assert
        val result = bookViewModel.bookUiState.value
        assertTrue(result is BookUiState.Error)
        println("Test resId: ${R.string.no_internet}")
        println("Production resId: ${((result as BookUiState.Error).message as UIText.StringResource).resId}")
        assertEquals(uiText, result.message)
    }
}