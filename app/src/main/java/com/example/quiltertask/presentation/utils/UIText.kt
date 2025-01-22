package com.example.quiltertask.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UIText {
    data class DynamicText(val value: String) : UIText()
    class StringResource(
        val resId: Int
    ) : UIText() {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is StringResource) return false
            return resId == other.resId
        }

        override fun hashCode(): Int {
            return resId
        }
    }

    @Composable
    fun asString(): String {
        return when(this){
            is DynamicText -> value
            is StringResource -> stringResource(id = resId)
        }
    }

}