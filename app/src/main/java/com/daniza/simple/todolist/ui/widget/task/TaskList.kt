package com.daniza.simple.todolist.ui.widget.task

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun TaskList(modifier: Modifier = Modifier) {
    val dummy = List<String>(10) { i -> "$i" }
    LazyColumn(modifier = modifier) {
        items(items = dummy) { item ->
            TaskItem(taskName = item)
        }
    }
}