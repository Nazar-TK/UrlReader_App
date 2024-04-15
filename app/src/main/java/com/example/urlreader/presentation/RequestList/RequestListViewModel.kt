package com.example.urlreader.presentation.RequestList

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.urlreader.data.Request
import com.example.urlreader.data.RequestRepository
import com.example.urlreader.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestListViewModel @Inject constructor(
    private val repository: RequestRepository): ViewModel() {

    private val _state = mutableStateOf(RequestListState())
    val state: State<RequestListState> = _state

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var getNumberJob: Job? = null

    private var deletedRequest: Request? = null

    init {
        getRequests()
    }

    fun onEvent(event: RequestListEvent) {
        when(event) {
            is RequestListEvent.OnDeleteRequestClick -> {
                viewModelScope.launch{
                    deletedRequest = event.request
                    repository.deleteRequest(event.request)
                    sendUiEvent(UiEvent.ShowSnackBar(message = "Request deleted", action = "Undo"))
                }
            }
            is RequestListEvent.OnUndoDeleteClick -> {
                deletedRequest?.let { request ->
                    viewModelScope.launch {
                        repository.insertRequest(request)
                    }
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun getRequests() {
        getNumberJob?.cancel()
        getNumberJob = repository.getRequests()
            .onEach { requests ->
                _state.value = state.value.copy(
                    requestListItems = requests
                )
            }.launchIn(viewModelScope)
    }
}