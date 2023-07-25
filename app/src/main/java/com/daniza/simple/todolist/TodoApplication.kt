package com.daniza.simple.todolist

import android.app.Application
import com.daniza.simple.todolist.data.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class TodoApplication : Application() {
    /*application scope object repository*/
    val repository: TodoRepository by lazy {
        ServiceLocator.provideTodoRepository(context = this)
    }

    /*global scope object repository for database io process*/
    val appCoroutine: CoroutineScope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }
}