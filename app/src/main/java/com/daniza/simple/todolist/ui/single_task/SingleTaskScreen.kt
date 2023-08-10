package com.daniza.simple.todolist.ui.single_task

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.daniza.simple.todolist.data.model.TaskModel
import com.daniza.simple.todolist.data.model.TaskTypeModel
import com.daniza.simple.todolist.ui.main.MainViewModel
import com.daniza.simple.todolist.ui.widget.common.ErrorScreen
import com.daniza.simple.todolist.ui.widget.common.LoadingScreen
import com.daniza.simple.todolist.data.source.Status
import com.daniza.simple.todolist.ui.widget.task.TaskDialog
import com.daniza.simple.todolist.ui.widget.task.TaskDialogType
import com.daniza.simple.todolist.ui.widget.task.TaskItem
import com.daniza.simple.todolist.ui.widget.task.TaskList
import com.daniza.simple.todolist.data.source.TaskUiState
import com.daniza.simple.todolist.ui.theme.CardColor
import com.daniza.simple.todolist.ui.widget.common.CustomColorSelector
import com.daniza.simple.todolist.ui.widget.common.CustomSelectorDialog

@Composable
fun SingleTaskScreen(
    mainViewModel: MainViewModel = viewModel(),
    type: Int? = 0,
    onPopBack: () -> Unit,
) {
    var showDialog by remember { mutableStateOf(TaskDialogType.NOTHING) }
    var stateTask by remember { mutableStateOf(TaskModel(type_id = type ?: 0)) }

    /* Listening to the parent which is TaskTypeModel within its value*/
    val taskTypeModel: TaskUiState<TaskTypeModel> by mainViewModel.singleTasksTypeData
        .collectAsStateWithLifecycle(initialValue = TaskUiState(isLoading = true))

    mainViewModel.getOneTaskType(type ?: 0)
    /* Listening into list of TaskModel, focusing on the list // Deprecated
    val listTask: TaskUiState<TaskModel> by mainViewModel.updateStateTaskType(type.toString())
        .collectAsStateWithLifecycle(
            initialValue = TaskUiState<TaskModel>(isLoading = true)
        )
     */

    /* Show the dialog first, double checking */
    when (showDialog) {
        TaskDialogType.COLOR_PICKER -> CustomSelectorDialog(
            value = taskTypeModel.dataSingle?.color ?: CardColor.NONE,
            callback = { color, status ->
            if (status == Status.DATA) mainViewModel.updateColorValue(
                taskTypeModel.dataSingle!!,
                color
            )
            Log.d("ASD", "SingleTaskScreen: ${color.ordinal}")
            showDialog = TaskDialogType.NOTHING
        })

        TaskDialogType.TASK_DELETE -> TaskDialog(
            type = TaskDialogType.DELETE,
            taskModel = stateTask.apply {
                title = "All of the data will be lost and cannot be undone"
            },
            callback = { model, status ->
                showDialog = TaskDialogType.NOTHING
                if (status == Status.DATA) {
                    mainViewModel.deleteTaskType(taskTypeModel.dataSingle!!)
                    onPopBack()
                }
            }
        )

        TaskDialogType.DELETE -> TaskDialog(
            TaskDialogType.DELETE,
            taskModel = stateTask,
            callback = { model, status ->
                showDialog = TaskDialogType.NOTHING
                if (status == Status.DATA) {
                    mainViewModel.deleteTask(model)
                }
            })

        TaskDialogType.EDIT -> TaskDialog(
            TaskDialogType.EDIT,
            taskModel = stateTask,
            callback = { model, status ->
                showDialog = TaskDialogType.NOTHING
                if (status == Status.DATA) {
                    mainViewModel.editTask(type ?: 0, stateTask, model)
                }
            })

        TaskDialogType.NEW -> TaskDialog(
            TaskDialogType.NEW,
            taskModel = TaskModel(id = 0),
            callback = { model, status ->
                showDialog = TaskDialogType.NOTHING
                if (status == Status.DATA) {
                    mainViewModel.saveNewTask(type ?: 0, model)
                }
            })

        else -> {}
    }

    when (taskTypeModel.status) {
        Status.DATA -> SingleTaskContent(
            taskType = taskTypeModel.dataSingle!!,
            listTask = taskTypeModel.dataSingle?.task_list!!,
            onCheckedTask = { task, b -> mainViewModel.updateCheckedTask(task, b) },
            onDeleteTask = { task -> showDialog = TaskDialogType.DELETE;stateTask = task },
            onEditTask = { task -> showDialog = TaskDialogType.EDIT;stateTask = task },
            onNewTask = { showDialog = TaskDialogType.NEW },
            onDeleteType = { showDialog = TaskDialogType.TASK_DELETE },
            onColorPicker = { showDialog = TaskDialogType.COLOR_PICKER }
        )

        Status.ERROR -> ErrorScreen(
            message = "Refresh please",
            onTimeout = { }
        )

        Status.LOADING -> LoadingScreen()
    }
}

@Composable
private fun SingleTaskContent(
    taskType: TaskTypeModel,
    listTask: List<TaskModel>?,
    onCheckedTask: (TaskModel, Boolean) -> Unit,
    onDeleteTask: (TaskModel) -> Unit,
    onEditTask: (TaskModel) -> Unit,
    onNewTask: () -> Unit,
    onDeleteType: () -> Unit,
    onColorPicker: () -> Unit,
) {
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
                progress = taskType.task_list_progress_percent,
                modifier = Modifier.padding(top = 8.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Column(
                modifier = Modifier
                    .padding(start = 24.dp)
                    .weight(1f)
            ) {
                Text(
                    text = taskType.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${taskType.task_list_size_inactive} of ${taskType.task_list_size} Task Done",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Divider(
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(top = 32.dp)
                )
            }
            Button(
                onClick = onDeleteType,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Red),
                modifier = Modifier.padding(end = 20.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "close",
                    tint = Color.Red
                )
                Text(text = "Delete", color = Color.Red)
            }
        }


        Row(
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            TextButton(
                onClick = onColorPicker,
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
                shape = RoundedCornerShape(8.dp),
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


        if (listTask.isNullOrEmpty()) {
            Text(
                text = "What you will be do?",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                textAlign = TextAlign.Center
            )
        } else {
            LazyColumn(
                modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp),
                userScrollEnabled = true,
            ) {
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
