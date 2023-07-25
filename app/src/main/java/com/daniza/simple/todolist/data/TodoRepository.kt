package com.daniza.simple.todolist.data

import com.daniza.simple.todolist.data.local.task.TaskDao
import com.daniza.simple.todolist.data.local.task.asDomainsModel
import com.daniza.simple.todolist.data.model.TaskModel
import com.daniza.simple.todolist.data.source.Result
import com.daniza.simple.todolist.data.source.TaskSourceData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*Several Todos:
* - create dummy data, or Fake Test to TodoRepositoryTest (testUnit)
* - also implement all methods under TaskSourceData
* - make sure all the test got passed
* */
class TodoRepository(
    private val taskDao: TaskDao,
    private val appCoroutine: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TaskSourceData {

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

    fun refresh(){
        observeTasks()
    }

    override fun setActiveStatus(task: TaskModel, status: Boolean) {

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