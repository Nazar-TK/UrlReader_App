package com.example.urlreader.presentation.RequestList

import com.example.urlreader.data.Request

sealed class RequestListEvent {
    data class OnDeleteRequestClick(val request: Request): RequestListEvent()
    data object OnUndoDeleteClick: RequestListEvent()
}
