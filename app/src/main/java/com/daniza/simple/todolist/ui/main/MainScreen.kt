package com.daniza.simple.todolist.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.daniza.simple.todolist.ui.theme.SimpleTodoListTheme
import com.daniza.simple.todolist.ui.widget.task.TaskList

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimpleTodoListTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            TaskList()
        }
    }
}