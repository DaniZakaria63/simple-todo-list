package com.daniza.simple.todolist.data.local.task

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun findAll(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE task_type_id = :taskId")
    fun findAllWithType(taskId: String) : Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun findOne(id: Int) : Flow<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveOne(task: TaskEntity)

    @Update
    fun updateOne(task: TaskEntity)

    @Delete
    fun deleteOne(task: TaskEntity)

    @Query("DELETE FROM tasks")
    fun deleteAll()
}