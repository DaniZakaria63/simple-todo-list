package com.daniza.simple.todolist.ui.widget.task

import androidx.compose.runtime.Composable
import com.daniza.simple.todolist.data.model.TaskModel

@Composable
fun DialogDeleteTask(
    taskModel: TaskModel,
    callback: (Status) -> Unit
){
    // TODO: Create alert dialog with two button callback
}

@Composable
fun DialogEditTask(
    taskModel: TaskModel,
    callback: (TaskModel, Status) -> Unit
){
    // TODO: Create dialog with input text
}

@Composable
fun DialogNewTask(
    callback: (TaskModel, Status) -> Unit
){
    // TODO: Show dialog and return the callback
}