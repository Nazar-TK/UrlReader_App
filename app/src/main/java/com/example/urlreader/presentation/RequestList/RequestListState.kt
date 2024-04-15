package com.example.urlreader.presentation.RequestList

import com.example.urlreader.data.Request

data class RequestListState(
    val requestListItems : List<Request> = emptyList(),
)
