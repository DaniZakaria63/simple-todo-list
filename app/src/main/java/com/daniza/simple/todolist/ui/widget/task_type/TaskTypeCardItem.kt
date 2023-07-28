package com.daniza.simple.todolist.ui.widget.task_type

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.daniza.simple.todolist.data.model.TaskModel


@Composable
fun TaskTypeCardItem(
    item: TaskModel,
    modifier: Modifier = Modifier
) {
    var checked by remember {
        mutableStateOf(item.checked)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { checked = it },
            colors = CheckboxDefaults.colors(
                checkmarkColor = Color.White,
                uncheckedColor = Color.White,
                checkedColor = Color.Gray
            )
        )
        Text(
            text = item.title, maxLines = 1, textAlign = TextAlign.Center,
            style = if (checked) TextStyle(textDecoration = TextDecoration.LineThrough)
            else MaterialTheme.typography.bodyLarge,
        )
    }
}
