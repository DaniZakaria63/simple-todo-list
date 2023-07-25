package com.daniza.simple.todolist.ui.widget.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.daniza.simple.todolist.data.model.TaskModel


@Composable
fun TaskList(
    listTask: List<TaskModel>,
    onCheckedTask: (TaskModel, Boolean) -> Unit,
    onDeleteTask: (TaskModel) -> Unit,
    onEditTask: (TaskModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(items = listTask, key = { task -> task.id }) { item ->
            Column(modifier = Modifier.fillMaxWidth()) {
                TaskItem(
                    task = item,
                    onCheckedChange = { check -> onCheckedTask(item, check) },
                    onDeleteClicked = { onDeleteTask(item)},
                    onEditClicked = { onEditTask(item) }
                )
                Divider(color = Color.DarkGray)
            }
        }
    }
}