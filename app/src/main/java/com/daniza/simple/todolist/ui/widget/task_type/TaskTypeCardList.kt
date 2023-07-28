package com.daniza.simple.todolist.ui.widget.task_type

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.daniza.simple.todolist.data.model.TaskModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskTypeCardList(
    modifier: Modifier = Modifier,
    items: List<TaskModel>
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 12.dp
        ),
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.Red,
            contentColor = Color.White
        ),
        onClick = { Log.d("TAG", "TaskTypeCardList: CLICKED") }
    ) {

        Column(
            modifier = Modifier.padding(
                start = 32.dp,
                top = 56.dp,
                bottom = 32.dp
            )
        ) {
            Text(
                text = "Trip To Paris",
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                modifier = Modifier.padding(end = 16.dp),
            )
            Divider(
                modifier = Modifier
                    .padding(vertical = 32.dp),
                color = Color.White
            )

            LazyColumn {
                items(items = items) { item ->
                    TaskTypeCardItem(item = item)
                }
            }

//            Button(
//                onClick = { /*TODO*/ },
//                colors = ButtonDefaults.buttonColors(contentColor = Color.White)
//            ) {
//                Icon(imageVector = Icons.Filled.ArrowRight, contentDescription = "more")
//                Text(text = "More")
//            }
        }

    }
}

//@Preview(showBackground = true, heightDp = 320, widthDp = 320)
@Composable
private fun TaskTypeCardItemPreview() {
    val items = List(4) { TaskModel(it, "List ke-$it") }
    TaskTypeCardList(
        modifier = Modifier.padding(12.dp),
        items = items
    )
}