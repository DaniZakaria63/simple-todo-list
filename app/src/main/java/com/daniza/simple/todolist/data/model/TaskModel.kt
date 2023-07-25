package com.daniza.simple.todolist.data.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.daniza.simple.todolist.data.local.task.TaskEntity

data class TaskModel(
    val id: Int = 0,
    val title: String = "-",
    val description: String = "-",
    val dueDate: String = "-",
    val isFinished: Boolean = false,
    val dateCreated: String = "-",
    val initialChecked: Boolean = false
) {
    // local mutable variable to saving
    var checked: Boolean by mutableStateOf(initialChecked)

    fun asDatabaseModel(): TaskEntity {
        return TaskEntity(id, title, description, dueDate, isFinished, dateCreated)
    }
}
