package com.daniza.simple.todolist.ui.widget.task

import com.daniza.simple.todolist.data.model.TaskModel

enum class Status {
    LOADING, ERROR, DATA
}

data class TaskUiState(
    val taskDatas: List<TaskModel>? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) {
    val status: Status
        get() = if (isLoading) {
            Status.LOADING
        } else if (isError) {
            Status.ERROR
        } else {
            Status.DATA
        }
}

sealed interface UiState {
    object Loading : UiState

    data class Success(
        val data: List<TaskModel>
    ) : UiState

    data class Error(
        val throwable: Throwable? = null
    ) : UiState
}