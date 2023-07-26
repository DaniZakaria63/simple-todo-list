package com.daniza.simple.todolist.ui.main

import com.daniza.simple.todolist.data.local.task.TaskDao
import com.daniza.simple.todolist.data.model.TaskModel
import com.daniza.simple.todolist.data.source.Result
import com.daniza.simple.todolist.data.source.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeTodoRepository(val dao: TaskDao): TaskRepository {
    override fun observeTasks(): Flow<Result<List<TaskModel>>> = flow {

    }

    override suspend fun getTask(taskId: Int): Flow<Result<TaskModel>> = flow {

    }

    override fun saveTask(task: TaskModel) {

    }

    override fun updateTask(task: TaskModel) {

    }

    override fun deleteTask(task: TaskModel) {

    }
}