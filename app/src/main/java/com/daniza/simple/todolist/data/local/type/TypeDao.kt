package com.daniza.simple.todolist.data.local.type

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.daniza.simple.todolist.data.local.task.TaskEntity
import kotlinx.coroutines.flow.Flow

/*tableName = task_type*/
@Dao
interface TypeDao {
    @Query("SELECT * FROM task_type WHERE id = :id")
    fun findOne(id: Int): Flow<TaskTypeEntity>

    @Query(
        "SELECT * FROM task_type LEFT JOIN tasks ON task_type.id = tasks.task_type_id" +
                " ORDER BY task_type.id"
    )
    fun findAllWithTask(): Flow<Map<TaskTypeEntity, List<TaskEntity>>>

    @Query(
        "SELECT * FROM task_type LEFT JOIN tasks ON task_type.id = tasks.task_type_id" +
                " WHERE task_type.id = :typeId ORDER BY task_type.id"
    )
    fun findOneWithTask(typeId: Int): Flow<Map<TaskTypeEntity, List<TaskEntity>>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOne(taskTypeEntity: TaskTypeEntity)

    @Update
    suspend fun updateOne(typeEntity: TaskTypeEntity)

    @Delete
    suspend fun deleteOne(typeEntity: TaskTypeEntity)


    /* analytic functionality */
    @Query("SELECT COUNT(*) AS total FROM task_type") // ALL TASK TYPE
    suspend fun countAllType(): Int

    @Query("SELECT * FROM task_type")
    suspend fun findAllTypeAsList() : List<TaskTypeEntity>
}