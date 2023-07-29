package com.daniza.simple.todolist.data.local.type

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.daniza.simple.todolist.data.local.task.TaskEntity
import kotlinx.coroutines.flow.Flow

/*tableName = task_type*/
@Dao
interface TypeDao {
    @Query("SELECT * FROM task_type WHERE id = :id")
    fun findOne(id: Int): Flow<TaskTypeEntity>

    @Query(
        "SELECT * FROM task_type LEFT JOIN tasks ON task_type.id = tasks.task_type_id" +
                " GROUP BY task_type.id ORDER BY task_type.id"
    )
    fun findAllWithTask(): Flow<Map<TaskTypeEntity, List<TaskEntity>>>

    @Query(
        "SELECT * FROM task_type LEFT JOIN tasks ON task_type.id = tasks.task_type_id" +
                " WHERE task_type.id = :typeId GROUP BY task_type.id ORDER BY task_type.id"
    )
    fun findOneWithTask(typeId: Int): Flow<Map<TaskTypeEntity, List<TaskEntity>>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveOne(taskTypeEntity: TaskTypeEntity)
}