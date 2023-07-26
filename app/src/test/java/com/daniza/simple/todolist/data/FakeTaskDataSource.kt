package com.daniza.simple.todolist.data

import com.daniza.simple.todolist.data.local.task.TaskDao
import com.daniza.simple.todolist.data.local.task.TaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeTaskDataSource(
    private val tasks: MutableList<TaskEntity> = mutableListOf()
) : TaskDao {

    override fun findAll(): Flow<List<TaskEntity>> = flow {
        emit(tasks)
    }

    override fun findOne(id: Int): Flow<TaskEntity> = flow {
        val task = tasks.find{ it.id == id}
        emit(task?: throw Exception("Did not found"))
    }

    override fun saveOne(task: TaskEntity) {
        tasks.add(task)
    }

    override fun updateOne(task: TaskEntity) {
        val index = tasks.indexOfFirst { it.id == task.id }
        tasks[index] = task
    }

    override fun deleteOne(task: TaskEntity) {
        tasks.remove(task)
    }

    override fun deleteAll() {
        tasks.removeAll(tasks)
    }
}