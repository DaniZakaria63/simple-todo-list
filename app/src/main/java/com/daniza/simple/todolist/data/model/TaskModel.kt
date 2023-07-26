package com.daniza.simple.todolist.data.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.daniza.simple.todolist.data.local.task.TaskEntity

data class TaskModel(
    var id: Int = 0,
    var title: String = "-",
    var description: String = "-",
    var dueDate: String = "-",
    var isFinished: Boolean = false,
    var dateCreated: String = "-"
) {
    // local mutable variable for keeping state
    var checked: Boolean by mutableStateOf(isFinished)

    fun asDatabaseModel(): TaskEntity {
        return TaskEntity(id, title, description, dueDate, isFinished, dateCreated)
    }
}
