package com.daniza.simple.todolist.data

import app.cash.turbine.test
import com.daniza.simple.todolist.data.local.task.TaskEntity
import com.daniza.simple.todolist.data.local.type.TaskTypeEntity
import com.daniza.simple.todolist.data.local.type.TypeDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class FakeTypeDataSource(
    private val types: MutableList<TaskTypeEntity> = mutableListOf(),
    private val tasks: FakeTaskDataSource,
    private val testScope: CoroutineScope = CoroutineScope(UnconfinedTestDispatcher())
) : TypeDao {
    override fun findOne(id: Int): Flow<TaskTypeEntity> = flow {
        emit(types.singleOrNull { it.id == id } ?: throw NoSuchElementException("Unknown Category"))
    }

    override fun findAllWithTask(): Flow<Map<TaskTypeEntity, List<TaskEntity>>> = flow {
        runBlocking {
            types.forEach { type ->
                tasks.findAllWithType(type.id.toString()).test {
                    val value: Map<TaskTypeEntity, List<TaskEntity>> = mapOf(type to awaitItem())
                    emit(value)
                }
            }
        }
    }

    override fun findOneWithTask(typeId: Int): Flow<Map<TaskTypeEntity, List<TaskEntity>>> = flow {
        types.find { it.id == typeId }?.let { type ->
            tasks.findAllWithType(type.id.toString()).collect {
                delay(200)
                val value: Map<TaskTypeEntity, List<TaskEntity>> = mapOf(type to it)
                emit(value)
            }
        } ?: throw NoSuchElementException("Unknown Category")
    }

    override suspend fun saveOne(taskTypeEntity: TaskTypeEntity) {
        types.add(taskTypeEntity)
    }

    override suspend fun updateOne(typeEntity: TaskTypeEntity) {
        types.indexOf(typeEntity).let {
            types.set(it, typeEntity)
        }
    }

    override suspend fun deleteOne(typeEntity: TaskTypeEntity) {
        types.indexOf(typeEntity).let {
            types.removeAt(it)
        }
    }

    override suspend fun countAllType(): Int {
        return types.count()
    }

    override suspend fun findAllTypeAsList(): List<TaskTypeEntity> {
        return types
    }
}