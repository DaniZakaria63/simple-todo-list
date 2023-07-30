package com.daniza.simple.todolist.ui.widget.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.daniza.simple.todolist.data.source.Status
import com.daniza.simple.todolist.ui.theme.CardColor


@Composable
fun CustomSelectorDialog(
    value: CardColor,
    callback: (CardColor, Status) -> Unit,
) {
    val cardColor = remember {
        mutableStateOf(CardColor.NONE)
    }
    Dialog(onDismissRequest = { callback(cardColor.value, Status.ERROR) }) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(12.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 24.dp, horizontal = 32.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = "Update Card Color",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 24.dp, bottom = 8.dp)
                        .align(Alignment.CenterHorizontally)
                )
                CustomColorSelector(
                    modifier = Modifier.padding(top = 8.dp),
                    value = value,
                    colorChanged = { cardColor.value = it })

                Button(
                    onClick = { callback(cardColor.value, Status.DATA) },
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Done")
                }
            }
        }
    }
}


@Composable
fun CustomColorSelector(
    value: CardColor,
    colorChanged: (CardColor) -> Unit,
    modifier: Modifier = Modifier
) {
    val radioOptions = listOf("Default", "Light Green", "Dark Green", "Black", "Grey")
    val radioOptionsValue = listOf(
        CardColor.NONE,
        CardColor.LIGHT_GREEN,
        CardColor.DARK_GREEN,
        CardColor.BLACK,
        CardColor.GREY
    )
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(value)
    }
    Column(modifier = modifier.selectableGroup()) {
        radioOptionsValue.forEachIndexed { index, color ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (color == selectedOption),
                        onClick = { onOptionSelected(color);colorChanged(color) },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 3.dp)
            ) {
                RadioButton(selected = (color == selectedOption), onClick = null)
                Text(
                    text = radioOptions[index],
                    modifier = Modifier.padding(start = 4.dp),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
