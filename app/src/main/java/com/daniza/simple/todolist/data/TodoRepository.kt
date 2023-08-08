package com.daniza.simple.todolist.data

import com.daniza.simple.todolist.data.local.task.TaskDao
import com.daniza.simple.todolist.data.local.task.TaskEntity
import com.daniza.simple.todolist.data.local.task.asDomainsModel
import com.daniza.simple.todolist.data.local.type.TaskTypeEntity
import com.daniza.simple.todolist.data.local.type.TypeDao
import com.daniza.simple.todolist.data.model.StatisticsModel
import com.daniza.simple.todolist.data.model.TaskAnalyticModel
import com.daniza.simple.todolist.data.model.TaskModel
import com.daniza.simple.todolist.data.model.TaskTypeModel
import com.daniza.simple.todolist.data.source.Result
import com.daniza.simple.todolist.data.source.TaskRepository
import com.daniza.simple.todolist.data.source.asResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException
import kotlin.jvm.Throws

const val DB_THROWABLE_MESSAGE = "Check application database for task type table"

class TodoRepository(
    private val taskDao: TaskDao,
    private val typeDao: TypeDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TaskRepository {

    /*Coroutine Scope run independently for avoiding state-loss in view-model*/
    private val scope = CoroutineScope(dispatcher + SupervisorJob())

    /*TaskType Data Provider and Manipulation*/
    override suspend fun observeTypes(): Flow<Result<List<TaskTypeModel>>> =
        withContext(dispatcher) {
            typeDao.findAllWithTask().map { map -> // Map<TaskTypeEntity, List<TaskEntity>>
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
    override suspend fun getTaskTypeOne(typeId: Int): Flow<Result<TaskTypeModel>> =
        withContext(dispatcher) {
            typeDao.findOneWithTask(typeId).map { map ->
                val extractedType: TaskTypeModel = parseTypeMapToList(map).get(0)
                Result.Success(extractedType)
            }.catch {
                Result.Error(Throwable(DB_THROWABLE_MESSAGE))
            }
        }

    private fun parseTypeMapToList(map: Map<TaskTypeEntity, List<TaskEntity>>): List<TaskTypeModel> {
        return map.keys.map { typeEntity ->
            val taskList: List<TaskEntity>? = map[typeEntity]
            typeEntity.toDomainModelWithTasks(
                if (taskList.isNullOrEmpty()) listOf() else taskList.asDomainsModel().sortedWith(
                    compareBy({ it.checked }, { it.id })
                )
            )
        }
    }


    @Throws(IllegalArgumentException::class)
    override fun saveTaskType(type: TaskTypeModel) {
        if (type.name.isEmpty() || type.name.equals("-")) throw IllegalArgumentException("Category name cannot be empty")
        scope.launch {
            typeDao.saveOne(type.asDatabaseModel())
        }
    }

    override fun deleteTaskType(type: TaskTypeModel) {
        scope.launch {
            typeDao.deleteOne(type.asDatabaseModel())
        }
    }


    override fun updateTypeColorValue(type: TaskTypeModel) {
        scope.launch {
            typeDao.updateOne(type.asDatabaseModel())
        }
    }

    /*Tasks Data Provider and Manipulation*/
    override suspend fun observeTasks(): Flow<Result<List<TaskModel>>> =
        withContext(dispatcher) {
            taskDao.findAll().map { value ->
                Result.Success(value.asDomainsModel())
            }.catch {
                Result.Error(Throwable(DB_THROWABLE_MESSAGE))
            }
        }

    override suspend fun getTask(taskId: Int): Flow<Result<TaskModel>> =
        withContext(dispatcher) {
            taskDao.findOne(taskId).map { value ->
                Result.Success(value.asDomainModel())
            }
        }


    override suspend fun observeTypeOne(typeId: String): Flow<Result<List<TaskModel>>> =
        withContext(dispatcher) {
            taskDao.findAllWithType(typeId).map { value ->
                Result.Success(value.asDomainsModel())
            }.catch {
                Result.Error(Throwable(DB_THROWABLE_MESSAGE))
            }
        }


    override fun saveTask(task: TaskModel) {
        scope.launch {
            taskDao.saveOne(task.asDatabaseModel())
        }
    }

    override fun updateTask(task: TaskModel) {
        scope.launch {
            taskDao.updateOne(task.asDatabaseModel())
        }
    }

    override fun deleteTask(task: TaskModel) {
        scope.launch {
            taskDao.deleteOne(task.asDatabaseModel())
        }
    }


    /* Statistical Code Type */
    override fun provideStatisticsData(): Flow<List<StatisticsModel>> = flow {
        val statistic: ArrayList<StatisticsModel> = ArrayList()
        val total_task_type: Int = provideAllTypeCount()
        val task_type_list: List<TaskTypeEntity> = provideAllTask()

        statistic.add(
            StatisticsModel(
                "Categories",
                "Total categories of task",
                total_task_type.toString()
            )
        )
        task_type_list.forEachIndexed { index, type ->
            val total_task_count = provideTaskCountByType(type.id)
            statistic.add(
                StatisticsModel(
                    type.name,
                    type.name,
                    total_task_count.toString()
                )
            )
        }

        val taskAnalytic: TaskAnalyticModel = provideAllTaskCount()
        statistic.add(StatisticsModel("Total Task", "", taskAnalytic.total.toString()))
        statistic.add(StatisticsModel("Finished Task", "", taskAnalytic.finished.toString()))
        statistic.add(StatisticsModel("Task Active", "", taskAnalytic.active.toString()))

        emit(statistic)
    }

    private suspend fun provideAllTaskCount(): TaskAnalyticModel {
        return taskDao.getAllTaskAnalytic()
    }

    private suspend fun provideAllTypeCount(): Int {
        return typeDao.countAllType()
    }

    private suspend fun provideTaskCountByType(typeId: Int): Int {
        return taskDao.getTaskCountFromTypeId(typeId)
    }

    private suspend fun provideAllTask(): List<TaskTypeEntity> {
        return typeDao.findAllTypeAsList()
    }
}