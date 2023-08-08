package com.daniza.simple.todolist.data

import com.daniza.simple.todolist.data.local.task.TaskDao
import com.daniza.simple.todolist.data.local.task.TaskEntity
import com.daniza.simple.todolist.data.model.TaskAnalyticModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeTaskDataSource(
    private val tasks: MutableList<TaskEntity> = mutableListOf()
) : TaskDao {

    override fun findAll(): Flow<List<TaskEntity>> = flow {
        emit(tasks)
    }

    override fun findAllWithType(taskId: String): Flow<List<TaskEntity>> = flow {
        emit(tasks.filter { it.task_type_id==taskId.toInt() })
    }

    override fun findOne(id: Int): Flow<TaskEntity> = flow {
        val task = tasks.find{ it.id == id}
        emit(task?: throw NoSuchElementException("Unknown Task"))
    }

    override suspend fun saveOne(task: TaskEntity) {
        tasks.add(task)
    }

    override suspend fun updateOne(task: TaskEntity) {
        val index = tasks.indexOfFirst { it.id == task.id }
        tasks[index] = task
    }

    override suspend fun deleteOne(task: TaskEntity) {
        tasks.remove(task)
    }

    override suspend fun deleteAll() {
        tasks.removeAll(tasks)
    }

    override suspend fun getAllTaskAnalytic(): TaskAnalyticModel {
        return TaskAnalyticModel()
    }

    override suspend fun getTaskCountFromTypeId(typeId: Int): Int {
        return tasks.count { it.task_type_id == typeId }
    }
}