package com.daniza.simple.todolist.data.local.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.daniza.simple.todolist.data.model.TaskModel

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "due_date") val dueDate: String,
    @ColumnInfo(name = "is_finished") val isFinished: Boolean,
    @ColumnInfo(name = "date_created") val dateCreated: String,
) {
    fun asDomainModel(): TaskModel {
        return TaskModel(
            id, title, description, dueDate, isFinished, dateCreated
        )
    }
}


fun List<TaskEntity>.asDomainsModel(): List<TaskModel> {
    return map { item ->
        TaskModel(
            item.id, item.title, item.description, item.dueDate, item.isFinished, item.dateCreated,
        )
    }
}