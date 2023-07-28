package com.daniza.simple.todolist.ui.single_task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daniza.simple.todolist.data.model.TaskModel
import com.daniza.simple.todolist.ui.main.MainViewModel
import com.daniza.simple.todolist.ui.widget.task.TaskItem
import com.daniza.simple.todolist.ui.widget.task.TaskUiState

@Composable
fun SingleTaskScreen(
    viewModel: MainViewModel,
    type: String? = ""
) {
    val taskTypeModel: TaskUiState = TaskUiState(taskDatas = null) // listen to Types data
    val taskList: TaskUiState = TaskUiState(
        taskDatas = listOf(
            TaskModel(title = "first list", description = "first description"),
            TaskModel(title = "the second list", description = "the second description"),
            TaskModel(title = "the third list", description = "the third description"),
            TaskModel(title = "first list", description = "first description"),
            TaskModel(title = "the second list", description = "the second description"),
            TaskModel(title = "the third list", description = "the third description"),
            TaskModel(title = "first list", description = "first description"),
            TaskModel(title = "the second list", description = "the second description"),
            TaskModel(title = "the third list", description = "the third description"),
            TaskModel(title = "first list", description = "first description"),
            TaskModel(title = "the second list", description = "the second description"),
            TaskModel(title = "the third list", description = "the third description"),
        )
    ) // listen to Tasks data

    SingleTaskContent(
        listTask = taskList.taskDatas,
        onCheckedTask = { model, b ->

        },
        onDeleteTask = { model ->

        },
        onEditTask = { model ->

        },
        onNewTask = {

        }
    )
}

@Composable
private fun SingleTaskContent(
    listTask: List<TaskModel>?,
    onCheckedTask: (TaskModel, Boolean) -> Unit,
    onDeleteTask: (TaskModel) -> Unit,
    onEditTask: (TaskModel) -> Unit,
    onNewTask: (() -> Unit),
) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 40.dp)
            ) {
                CircularProgressIndicator(
                    0.75f,
                    modifier = Modifier.padding(top = 8.dp),
                    strokeWidth = 1.dp
                )
                Column(
                    modifier = Modifier
                        .padding(start = 24.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = "My Task",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "3 of 4 Task",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Divider(color = Color.LightGray, modifier = Modifier.padding(top = 32.dp))
                }
            }

            if (listTask.isNullOrEmpty()) {
                Text(
                    text = "What you will be do?",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    modifier = Modifier.padding(top = 12.dp),
                    userScrollEnabled = true,
                ) {
                    item {
                        Row(
                            modifier = Modifier.padding(vertical = 12.dp)
                        ) {
                            TextButton(
                                onClick = {},
                                modifier = Modifier.padding(start = 24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ColorLens,
                                    contentDescription = "pick_color"
                                )
                                Text(
                                    text = "Pick Color",
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Button(
                                onClick = onNewTask,
                                shape = RectangleShape,
                                modifier = Modifier
                                    .padding(end = 24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "",
                                    modifier = Modifier.padding(0.dp)
                                )
                                Text(text = "Add Task")
                            }
                        }
                    }
                    items(items = listTask) { item ->
                        TaskItem(
                            task = item,
                            onCheckedChange = { check -> onCheckedTask(item, check) },
                            onDeleteClicked = { onDeleteTask(item) },
                            onEditClicked = { onEditTask(item) }
                        )
                    }
                }
            }
        }
    }
}