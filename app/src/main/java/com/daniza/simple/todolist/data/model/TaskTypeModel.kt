package com.daniza.simple.todolist.data.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.daniza.simple.todolist.data.local.type.TaskTypeEntity
import com.daniza.simple.todolist.ui.theme.CardColor

data class TaskTypeModel(
    val id: Int = 0,
    val name: String = "",
    var color: CardColor = CardColor.NONE,
    val date_created: String = "",
    var _task_list: List<TaskModel> = listOf()
) {
    val task_list_size: Int get() = _task_list.size
    val task_list_size_inactive: Int get() = _task_list.count { it.checked }
    val task_list_size_active: Int get() = task_list_size - task_list_size_inactive

    val task_list by mutableStateOf(_task_list)

    fun asDatabaseModel(): TaskTypeEntity {
        return TaskTypeEntity(
            id, name, color, date_created
        )
    }
}