package com.daniza.simple.todolist.ui.widget.task

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.UnfoldLess
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniza.simple.todolist.R
import com.daniza.simple.todolist.data.model.TaskModel


@Composable
fun TaskItem(
    task: TaskModel,
    onCheckedChange: (Boolean) -> Unit,
    onEditClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Checkbox(checked = task.checked, onCheckedChange = onCheckedChange)
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    task.title,
                    style = if (task.checked) TextStyle(textDecoration = TextDecoration.LineThrough)
                    else MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f),
                    maxLines = 2
                )
                Text(
                    text = task.dueDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )
            }
            if (expanded) {
                Text(
                    text = task.description,
                    modifier = Modifier.padding(vertical = 4.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(modifier = Modifier.padding(vertical = 4.dp)) {

                    Button(
                        onClick = onDeleteClicked,
                        modifier = Modifier
                            .padding(end = 4.dp),
                        colors = ButtonDefaults.buttonColors(Color.Red)
                    ) {
                        Text(text = "Delete")
                    }
                    Button(
                        onClick = onEditClicked, modifier = Modifier
                            .padding(start = 4.dp),
                        colors = ButtonDefaults.buttonColors(Color.Blue)
                    ) {
                        Text(text = "Edit")
                    }
                }
            }
        }
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (expanded) {
                    stringResource(id = R.string.expand_less)
                } else {
                    stringResource(id = R.string.expand_more)
                }
            )
        }
    }
}