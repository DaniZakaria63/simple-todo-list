package com.daniza.simple.todolist.data.model

import com.daniza.simple.todolist.data.local.task.TaskEntity

data class TaskModel(
    val id: Int = 0
) {
    fun asDatabaseModel(): TaskEntity {
        return TaskEntity(
            id
        )
    }
}
