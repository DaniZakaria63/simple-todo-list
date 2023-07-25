package com.daniza.simple.todolist.data.local.task

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.daniza.simple.todolist.data.model.TaskModel

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,

){
    fun asDomainModel() : TaskModel{
        return TaskModel()
    }
}


fun List<TaskEntity>.asDomainsModel() : List<TaskModel>{
    return map { item -> TaskModel() }
}