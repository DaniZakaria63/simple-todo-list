package com.daniza.simple.todolist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.daniza.simple.todolist.data.local.task.TaskDao
import com.daniza.simple.todolist.data.local.task.TaskEntity
import com.daniza.simple.todolist.data.local.type.TaskTypeEntity
import com.daniza.simple.todolist.data.local.type.TypeDao

@Database(
    entities = [TaskEntity::class, TaskTypeEntity::class],
    exportSchema = false,
    version = 2
)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun taskDao() : TaskDao
    abstract fun typeDao() : TypeDao

    companion object{
        const val DB_NAME = "todo_list"
    }
}