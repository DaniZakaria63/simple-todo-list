package com.daniza.simple.todolist.ui.main

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
import com.daniza.simple.todolist.ui.widget.common.ErrorScreen
import com.daniza.simple.todolist.ui.widget.common.LoadingScreen
import com.daniza.simple.todolist.ui.widget.task.Status
import com.daniza.simple.todolist.ui.widget.task.TaskList
import com.daniza.simple.todolist.ui.widget.task.TaskUiState

@Composable
fun TodoListScene(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {

    val listTask: TaskUiState by viewModel.allListData.collectAsStateWithLifecycle(
        initialValue = TaskUiState(isLoading = true)
    )

    var showDialog by remember { mutableStateOf(listOf(false, false, false)) }

    // Just pass it through without any checking
    val onCheckedTask: (TaskModel, Boolean) -> Unit =
        { task, b -> viewModel.updateCheckedTask(task, b) }

    // Show the dialog first, double checking
    val onDeleteTask: (TaskModel) -> Unit = { task ->
        if(showDialog[2]){

        }
    }

    // Show the dialog and prepare to cancel it
    val onEditTask: (TaskModel) -> Unit = { task ->
        if(showDialog[1]){

        }
    }

    // Show the add new task dialog
    val onNewTask: () -> Unit = {
        if(showDialog[0]){

        }
    }

    when (listTask.status) {
        Status.DATA -> TodoListContent(
            listTask = listTask.taskDatas!!,
            onCheckedTask = onCheckedTask,
            onDeleteTask = onDeleteTask,
            onEditTask = onEditTask,
            onNewTask = onNewTask
        )

        Status.ERROR -> ErrorScreen(
            message = "Refresh please",
            onTimeout = {/*TODO: Refresh the data*/ }
        )

        Status.LOADING -> LoadingScreen()
    }
}

@Composable
fun TodoListContent(
    listTask: List<TaskModel>,
    onCheckedTask: (TaskModel, Boolean) -> Unit,
    onDeleteTask: (TaskModel) -> Unit,
    onEditTask: (TaskModel) -> Unit,
    onNewTask: () -> Unit,
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