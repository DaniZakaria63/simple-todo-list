package com.daniza.simple.todolist.ui.main

import com.daniza.simple.todolist.data.local.task.TaskDao
import com.daniza.simple.todolist.data.model.StatisticsModel
import com.daniza.simple.todolist.data.model.TaskModel
import com.daniza.simple.todolist.data.model.TaskTypeModel
import com.daniza.simple.todolist.data.source.Result
import com.daniza.simple.todolist.data.source.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeTodoRepository(val dao: TaskDao): TaskRepository {
    override suspend fun observeTypes(): Flow<Result<List<TaskTypeModel>>> {
        TODO("Not yet implemented")
    }

    override suspend fun observeTasks(): Flow<Result<List<TaskModel>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTask(taskId: Int): Flow<Result<TaskModel>> {
        TODO("Not yet implemented")
    }

    override fun saveTask(task: TaskModel) {
        TODO("Not yet implemented")
    }

    override fun updateTask(task: TaskModel) {
        TODO("Not yet implemented")
    }

    override fun deleteTask(task: TaskModel) {
        TODO("Not yet implemented")
    }

    override fun saveTaskType(type: TaskTypeModel) {
        TODO("Not yet implemented")
    }

    override suspend fun observeTypeOne(typeId: String): Flow<Result<List<TaskModel>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskTypeOne(typeId: Int): Flow<Result<TaskTypeModel>> {
        TODO("Not yet implemented")
    }

    override fun deleteTaskType(type: TaskTypeModel) {
        TODO("Not yet implemented")
    }

    override fun updateTypeColorValue(type: TaskTypeModel) {
        TODO("Not yet implemented")
    }

    override fun provideStatisticsData(): Flow<List<StatisticsModel>> {
        TODO("Not yet implemented")
    }
}