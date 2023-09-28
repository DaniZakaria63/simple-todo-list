package com.daniza.simple.todolist

import android.app.Application
import com.daniza.simple.todolist.data.TodoRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class TodoApplication : Application()