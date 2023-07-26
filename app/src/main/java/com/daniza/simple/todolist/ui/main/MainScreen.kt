package com.daniza.simple.todolist.ui.main

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.daniza.simple.todolist.data.model.TaskModel
import com.daniza.simple.todolist.ui.splash.SplashScreen
import com.daniza.simple.todolist.ui.widget.common.ErrorScreen
import com.daniza.simple.todolist.ui.widget.common.LoadingScreen
import com.daniza.simple.todolist.ui.widget.task.Status
import com.daniza.simple.todolist.ui.widget.task.TaskDialog
import com.daniza.simple.todolist.ui.widget.task.TaskDialogType
import com.daniza.simple.todolist.ui.widget.task.TaskList
import com.daniza.simple.todolist.ui.widget.task.TaskUiState

@Composable
fun MainScreen(viewModel: MainViewModel) {
    Surface(color = MaterialTheme.colorScheme.primary) {
        var showSplashScreen by remember { mutableStateOf(false) }
        if (showSplashScreen) {
            SplashScreen {
                showSplashScreen = false
            }
        } else {
            TodoListScene(mainViewModel = viewModel, modifier = Modifier.fillMaxSize())
        }
    }
}


@Composable
private fun TodoListScene(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel()
) {

    var showDialog by remember { mutableStateOf("") }
    var stateTask by remember { mutableStateOf(TaskModel()) }

    val listTask: TaskUiState by mainViewModel.allListData.collectAsStateWithLifecycle(
        initialValue = TaskUiState(isLoading = true)
    )

    /* Just pass it through without any checking */
    val onCheckedTask: (TaskModel, Boolean) -> Unit =
        { task, b -> mainViewModel.updateCheckedTask(task, b) }

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
                if (status == Status.DATA) mainViewModel.editTask(stateTask,model);showDialog = ""
            })

        "NEW" -> TaskDialog(
            TaskDialogType.NEW,
            taskModel = TaskModel(id = 0),
            callback = { model, status ->
                if (status == Status.DATA) mainViewModel.saveNewTask(model);showDialog = ""
            })
    }

    when (listTask.status) {
        Status.DATA -> TodoListContent(
            listTask = listTask.taskDatas!!,
            onCheckedTask = onCheckedTask,
            onDeleteTask = { task -> showDialog = "DELETE";stateTask = task },
            onEditTask = { task -> showDialog = "EDIT";stateTask = task },
            onNewTask = { showDialog = "NEW" }
        )

        Status.ERROR -> ErrorScreen(
            message = "Refresh please",
            onTimeout = { mainViewModel.forceRefresh() }
        )

        Status.LOADING -> LoadingScreen()
    }
}

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