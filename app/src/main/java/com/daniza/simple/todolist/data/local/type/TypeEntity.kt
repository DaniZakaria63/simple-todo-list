package com.daniza.simple.todolist.data.local.type

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.daniza.simple.todolist.data.model.TaskModel
import com.daniza.simple.todolist.data.model.TaskTypeModel
import com.daniza.simple.todolist.ui.theme.CardColor

@Entity(tableName = "task_type")
data class TaskTypeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "color") val color: CardColor,
    @ColumnInfo(name = "date_created") val date_created: String,
){
    fun toDomainModelWithTasks(tasks: List<TaskModel>) : TaskTypeModel{
        return TaskTypeModel(
            id, name, color, date_created,
            _task_list = tasks
        )
    }
}