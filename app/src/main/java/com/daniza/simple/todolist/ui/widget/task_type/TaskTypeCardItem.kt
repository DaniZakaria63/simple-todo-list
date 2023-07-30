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

/*Card item for showing latest active task in the beginning of category/type task
* each items have checked event trigger
* limitations:
* - should not have any attribute edit except checked
* - do not have any deleted event
* - shows tasks in max. 4 rows
* */
@Composable
fun TaskTypeCardItem(
    modifier: Modifier = Modifier,
    item: TaskModel,
    onCheckChange: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.checked,
            onCheckedChange = onCheckChange,
            colors = CheckboxDefaults.colors(
                checkmarkColor = Color.White,
                uncheckedColor = Color.White,
                checkedColor = Color.Gray
            )
        )
        Text(
            text = item.title, maxLines = 1, textAlign = TextAlign.Center,
            style = if (item.checked) TextStyle(textDecoration = TextDecoration.LineThrough)
            else MaterialTheme.typography.labelLarge,
        )
    }
}
