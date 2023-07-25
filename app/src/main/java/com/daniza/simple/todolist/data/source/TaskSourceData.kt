package com.daniza.simple.todolist.data.source

import com.daniza.simple.todolist.data.model.TaskModel
import kotlinx.coroutines.flow.Flow

interface TaskSourceData {

    fun observeTasks(): Flow<Result<List<TaskModel>>>

    suspend fun getTask(taskId: Int): Flow<Result<TaskModel>>

    fun saveTask(task: TaskModel)

    fun setActiveStatus(task: TaskModel, status: Boolean)

    fun updateTask(task: TaskModel)

    fun deleteTask(task: TaskModel)

}