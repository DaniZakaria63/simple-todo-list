package com.daniza.simple.todolist.data.source

import com.daniza.simple.todolist.data.model.TaskModel
import com.daniza.simple.todolist.data.model.TaskTypeModel
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun observeTypes(): Flow<Result<List<TaskTypeModel>>>

    fun observeTasks(): Flow<Result<List<TaskModel>>>

    suspend fun getTask(taskId: Int): Flow<Result<TaskModel>>

    fun saveTask(task: TaskModel)

    fun updateTask(task: TaskModel)

    fun deleteTask(task: TaskModel)

    fun saveTaskType(type: TaskTypeModel)

    fun observeTypeOne(typeId: String): Flow<Result<List<TaskModel>>>

    fun getTaskTypeOne(typeId: Int) : Flow<Result<TaskTypeModel>>

    fun deleteTaskType(type: TaskTypeModel)
}