package com.daniza.simple.todolist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.daniza.simple.todolist.data.local.task.TaskDao
import com.daniza.simple.todolist.data.local.task.TaskEntity

@Database(
    entities = [TaskEntity::class],
    exportSchema = false,
    version = 1
)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun taskDao() : TaskDao

    companion object{
        const val DB_NAME = "todo_list"
    }
}