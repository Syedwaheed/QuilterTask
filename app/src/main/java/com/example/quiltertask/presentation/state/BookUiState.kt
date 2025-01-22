package com.example.quiltertask.presentation.state

import com.example.quiltertask.presentation.utils.UIText

sealed class BookUiState<out T> {
    data object Loading : BookUiState<Nothing>()
    data class Success<out T>(val data: T) : BookUiState<T>()
    data class Error(val message: UIText) : BookUiState<Nothing>()
}