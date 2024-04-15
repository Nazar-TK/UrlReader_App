package com.example.urlreader.utils

sealed class UiEvent {
    data class ShowSnackBar(
        val message: String,
        val action: String? = null
    ) : UiEvent()
}
