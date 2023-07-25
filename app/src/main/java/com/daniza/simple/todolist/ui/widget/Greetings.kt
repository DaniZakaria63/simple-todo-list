package com.daniza.simple.todolist.ui.widget

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
private fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun Greetings(modifier: Modifier = Modifier) {
    val dummy = List<String>(10) { i -> "$i" }
    LazyColumn(modifier = modifier) {
        items(items = dummy) { item ->
            Greeting(name = item)
        }
    }
}