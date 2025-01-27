package com.example.quiltertask.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quiltertask.domain.model.Book
import com.example.quiltertask.domain.repository.ErrorMapper
import com.example.quiltertask.domain.usecase.GetBookUseCase
import com.example.quiltertask.presentation.state.BookUiState
import com.example.quiltertask.presentation.utils.asUiText
import com.example.quiltertask.data.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.jetbrains.annotations.Async.Schedule
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val getBookUseCase: GetBookUseCase,
    private val errorMapper: ErrorMapper
) : ViewModel() {

    private val _bookUiState = MutableStateFlow<BookUiState<List<Book?>?>>(BookUiState.Loading)
    val bookUiState: StateFlow<BookUiState<List<Book?>?>> = _bookUiState.asStateFlow()

    private val compositeDisposable = CompositeDisposable()

    init {
        fetchBooks()
    }

    fun fetchBooks() {
        getBookUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    when (result) {
                        is Result.Success -> _bookUiState.value =
                            BookUiState.Success(result.data)

                        is Result.Error -> _bookUiState.value =
                            BookUiState.Error(result.error.asUiText())
                    }
                },
                { throwable ->
                    _bookUiState.value =
                        BookUiState.Error(errorMapper.mapError(throwable).asUiText())
                }
            ).also {
                compositeDisposable.add(it)
            }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}





