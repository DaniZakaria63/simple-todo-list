package com.daniza.simple.todolist.data.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

data class TaskTypeModel(
    val id: Int = 0,
    val name: String = "",
    val date_created: String = "",
    var _task_list: List<TaskModel> = listOf()
) {
    val task_list_size get() = _task_list.size

    val task_list by mutableStateOf(_task_list)
}