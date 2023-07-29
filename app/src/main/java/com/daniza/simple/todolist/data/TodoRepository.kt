package com.daniza.simple.todolist.data

import com.daniza.simple.todolist.data.local.task.TaskDao
import com.daniza.simple.todolist.data.local.task.TaskEntity
import com.daniza.simple.todolist.data.local.task.asDomainsModel
import com.daniza.simple.todolist.data.local.type.TypeDao
import com.daniza.simple.todolist.data.model.TaskModel
import com.daniza.simple.todolist.data.model.TaskTypeModel
import com.daniza.simple.todolist.data.source.Result
import com.daniza.simple.todolist.data.source.TaskRepository
import com.daniza.simple.todolist.data.source.asResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoRepository(
    private val taskDao: TaskDao,
    private val typeDao: TypeDao,
    private val appCoroutine: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TaskRepository {

    /*TaskType Data Provider and Manipulation*/
    override fun observeTypes(): Flow<Result<List<TaskTypeModel>>> {
        return typeDao.findAllWithTask().map { map -> // Map<TaskTypeEntity, List<TaskEntity>>
            val extractedType: List<TaskTypeModel> = map.keys.map { typeEntity ->
                val taskList: List<TaskEntity>? = map[typeEntity]
                typeEntity.toDomainModelWithTasks(
                    if (taskList.isNullOrEmpty()) listOf() else taskList.asDomainsModel()
                )
            }
            Result.Success(extractedType)
        }.catch {
            Result.Error(Throwable("Check application database for task type table"))
        }
    }

    /*Tasks Data Provider and Manipulation*/
    override fun observeTasks(): Flow<Result<List<TaskModel>>> {
        return taskDao.findAll().map { value ->
            Result.Success(value.asDomainsModel())
        }.catch {
            Result.Error(Throwable("Check your database for tasks table"))
        }
    }

    override suspend fun getTask(taskId: Int): Flow<Result<TaskModel>> {
        return taskDao.findOne(taskId).map { value ->
            Result.Success(value.asDomainModel())
        }
    }

    override fun saveTask(task: TaskModel) {
        appCoroutine.launch {
            withContext(ioDispatcher) {
                taskDao.saveOne(task.asDatabaseModel())
            }
        }
    }

    override fun updateTask(task: TaskModel) {
        appCoroutine.launch {
            withContext(ioDispatcher) {
                taskDao.updateOne(task.asDatabaseModel())
            }
        }
    }

    override fun deleteTask(task: TaskModel) {
        appCoroutine.launch {
            withContext(ioDispatcher) {
                taskDao.deleteOne(task.asDatabaseModel())
            }
        }
    }
}