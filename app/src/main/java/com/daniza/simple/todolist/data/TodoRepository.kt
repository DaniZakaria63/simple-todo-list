package com.daniza.simple.todolist.data

import com.daniza.simple.todolist.data.local.task.TaskDao
import com.daniza.simple.todolist.data.local.task.TaskEntity
import com.daniza.simple.todolist.data.local.task.asDomainsModel
import com.daniza.simple.todolist.data.local.type.TaskTypeEntity
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

const val DB_THROWABLE_MESSAGE = "Check application database for task type table"
class TodoRepository(
    private val taskDao: TaskDao,
    private val typeDao: TypeDao,
    private val appCoroutine: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TaskRepository {

    /*TaskType Data Provider and Manipulation*/
    override fun observeTypes(): Flow<Result<List<TaskTypeModel>>> {
        return typeDao.findAllWithTask().map { map -> // Map<TaskTypeEntity, List<TaskEntity>>
            val extractedType: List<TaskTypeModel> = parseTypeMapToList(map)
            Result.Success(extractedType)
        }.catch {
            Result.Error(Throwable(DB_THROWABLE_MESSAGE))
        }
    }

    /*the data is to:
    * - get count of all task active
    * - also all task inactive
    * - get the title
    * - get also the color (optional)
    * */
    override fun getTaskTypeOne(typeId: Int): Flow<Result<TaskTypeModel>> {
        return typeDao.findOneWithTask(typeId).map { map ->
            val extractedType: TaskTypeModel = parseTypeMapToList(map).get(0)
            Result.Success(extractedType)
        }.catch {
            Result.Error(Throwable(DB_THROWABLE_MESSAGE))
        }
    }

    private fun parseTypeMapToList(map: Map<TaskTypeEntity, List<TaskEntity>>) : List<TaskTypeModel>{
        return map.keys.map { typeEntity ->
            val taskList: List<TaskEntity>? = map[typeEntity]
            typeEntity.toDomainModelWithTasks(
                if (taskList.isNullOrEmpty()) listOf() else taskList.asDomainsModel()
            )
        }
    }


    override fun saveTaskType(type: TaskTypeModel) {
        appCoroutine.launch {
            launch { withContext(ioDispatcher) { typeDao.saveOne(type.asDatabaseModel()) } }
        }
    }


    /*Tasks Data Provider and Manipulation*/
    override fun observeTasks(): Flow<Result<List<TaskModel>>> {
        return taskDao.findAll().map { value ->
            Result.Success(value.asDomainsModel())
        }.catch {
            Result.Error(Throwable(DB_THROWABLE_MESSAGE))
        }
    }

    override suspend fun getTask(taskId: Int): Flow<Result<TaskModel>> {
        return taskDao.findOne(taskId).map { value ->
            Result.Success(value.asDomainModel())
        }
    }


    override fun observeTypeOne(typeId: String): Flow<Result<List<TaskModel>>> {
        return taskDao.findAllWithType(typeId).map { value ->
            Result.Success(value.asDomainsModel())
        }.catch {
            Result.Error(Throwable(DB_THROWABLE_MESSAGE))
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