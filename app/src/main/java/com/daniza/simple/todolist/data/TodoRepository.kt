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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
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


    override fun saveTaskType(type: TaskTypeModel) {
        appCoroutine.launch {
            launch { withContext(ioDispatcher) { typeDao.saveOne(type.asDatabaseModel()) } }
        }
    }

    override fun deleteTaskType(type: TaskTypeModel) {
        appCoroutine.launch {
            launch { withContext(ioDispatcher) { typeDao.deleteOne(type.asDatabaseModel()) } }
        }
    }


    override fun updateTypeColorValue(type: TaskTypeModel) {
        appCoroutine.launch {
            launch { withContext(ioDispatcher) { typeDao.updateOne(type.asDatabaseModel()) } }
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


    /* Statistical Code Type */
    override fun provideStatisticsData(): Flow<List<StatisticsModel>> = flow {
        val statistic: ArrayList<StatisticsModel> = ArrayList()
        val total_task_type: Int = provideAllTypeCount()
        val task_type_list: List<TaskTypeEntity> = provideAllTask()

        statistic.add(
            StatisticsModel(
                "Total Categories",
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
        statistic.add(StatisticsModel("Total Task Listed", "", taskAnalytic.total.toString()))
        statistic.add(StatisticsModel("Total Finished Task", "", taskAnalytic.finished.toString()))
        statistic.add(StatisticsModel("Total Active Task", "", taskAnalytic.active.toString()))

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