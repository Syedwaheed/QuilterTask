package com.example.quiltertask.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiltertask.domain.model.Book
import com.example.quiltertask.domain.repository.ErrorMapper
import com.example.quiltertask.domain.usecase.GetBookUseCase
import com.example.quiltertask.presentation.state.BookUiState
import com.example.quiltertask.presentation.utils.asUiText
import com.example.quiltertask.data.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val getBookUseCase: GetBookUseCase,
    private val errorMapper: ErrorMapper
) : ViewModel() {

    private val _bookUiState = MutableStateFlow<BookUiState<List<Book?>?>>(BookUiState.Loading)
    val bookUiState: StateFlow<BookUiState<List<Book?>?>> = _bookUiState.asStateFlow()

    init {
        fetchBooks()
    }

    fun fetchBooks() {
        viewModelScope.launch {
            getBookUseCase()
                .onEach { result ->
                    when (result) {
                        is Result.Success -> _bookUiState.value = BookUiState.Success(result.data)
                        is Result.Error -> _bookUiState.value = BookUiState.Error(result.error.asUiText())
                    }
                }
                .catch { error ->
                    _bookUiState.value = BookUiState.Error(errorMapper.mapError(error).asUiText())
                }
                .collect()
        }
    }
}





