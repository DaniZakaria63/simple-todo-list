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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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


val taskListDummy: TaskUiState<TaskModel> = TaskUiState(
    dataList = listOf(
        TaskModel(
            title = "first list",
            description = "first description",
            dueDate = "2021-2-2"
        ),
        TaskModel(
            title = "the second list",
            description = "the second description",
            dueDate = "2021-2-2"
        ),
        TaskModel(
            title = "the third list",
            description = "the third description",
            dueDate = "2021-2-2"
        ),
        TaskModel(
            title = "first list",
            description = "first description",
            dueDate = "2021-2-2"
        ),
        TaskModel(
            title = "first list",
            description = "first description",
            dueDate = "2021-2-2"
        ),
    )
) // listen to Tasks data

@Composable
fun SingleTaskScreen(
    mainViewModel: MainViewModel,
    type: Int? = 0
) {
    var showDialog by remember { mutableStateOf("") }
    var stateTask by remember { mutableStateOf(TaskModel(type_id = type?:0)) }

    /* Listening to the parent which is TaskTypeModel within its value*/
    val taskTypeModel: TaskUiState<TaskTypeModel> by mainViewModel.getOneTaskType(type?:0)
        .collectAsStateWithLifecycle(initialValue = TaskUiState(isLoading = true))

    /* Listening into list of TaskModel, focusing on the list // Deprecated
    val listTask: TaskUiState<TaskModel> by mainViewModel.updateStateTaskType(type.toString())
        .collectAsStateWithLifecycle(
            initialValue = TaskUiState<TaskModel>(isLoading = true)
        )
     */

    /* Show the dialog first, double checking */
    when (showDialog) {
        "DELETE" -> TaskDialog(
            TaskDialogType.DELETE,
            taskModel = stateTask,
            callback = { model, status ->
                if (status == Status.DATA) mainViewModel.deleteTask(model);showDialog = ""
            })

        "EDIT" -> TaskDialog(
            TaskDialogType.EDIT,
            taskModel = stateTask,
            callback = { model, status ->
                Log.d("ASD", "TodoListScene: $status")
                if (status == Status.DATA) mainViewModel.editTask(type?:0, stateTask, model);showDialog = ""
            })

        "NEW" -> TaskDialog(
            TaskDialogType.NEW,
            taskModel = TaskModel(id = 0),
            callback = { model, status ->
                if (status == Status.DATA) mainViewModel.saveNewTask(type?:0,model);showDialog = ""
            })
    }

    when (taskTypeModel.status) {
        Status.DATA -> SingleTaskContent(
            taskType = taskTypeModel.dataSingle!!,
            listTask = taskTypeModel.dataSingle?.task_list!!,
            onCheckedTask = { task, b -> mainViewModel.updateCheckedTask(task, b)},
            onDeleteTask = { task -> showDialog = "DELETE";stateTask = task },
            onEditTask = { task -> showDialog = "EDIT";stateTask = task },
            onNewTask = { showDialog = "NEW" }
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
                    Divider(color = Color.LightGray, modifier = Modifier.padding(top = 32.dp))
                }
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, Color.Red),
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "close",
                        tint = Color.Red
                    )
                }
            }


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
                    modifier = Modifier.padding(top = 12.dp),
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
}

/* OLD VERSION CODE (deprecated)
*  Change to SingleTaskContent instead
* */

@Composable
private fun TodoListContent(
    listTask: List<TaskModel>,
    onCheckedTask: (TaskModel, Boolean) -> Unit,
    onDeleteTask: (TaskModel) -> Unit,
    onEditTask: (TaskModel) -> Unit,
    onNewTask: (() -> Unit),
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(
                    vertical = 20.dp,
                    horizontal = 24.dp
                )
        ) {
            Text(
                text = "Simple Todo List",
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.titleMedium.copy(color = Color.Black)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {

                TaskList(
                    listTask = listTask,
                    onCheckedTask = onCheckedTask,
                    onDeleteTask = onDeleteTask,
                    onEditTask = onEditTask
                )

                // Show the FAB
                val showFab by remember {
                    mutableStateOf(true)
                }

                if (showFab) {
                    FloatingActionButton(
                        containerColor = MaterialTheme.colorScheme.primary,
                        onClick = onNewTask,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .navigationBarsPadding()
                            .padding(bottom = 8.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "")
                    }
                }
            }
        }
    }
}
