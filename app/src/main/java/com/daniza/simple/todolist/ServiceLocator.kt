package com.daniza.simple.todolist

import android.content.Context
import com.daniza.simple.todolist.data.TodoRepository
import com.daniza.simple.todolist.data.local.TodoDatabase

object ServiceLocator {
    @Volatile
    private var database: TodoDatabase? = null

    var repository: TodoRepository? = null

    fun provideTodoRepository(context: Context): TodoRepository {
        // TODO: Implement repository synchronization
        return repository!!
    }

    // TODO: Initialize Repository and Database instance
}