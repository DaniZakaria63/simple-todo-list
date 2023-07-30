package com.daniza.simple.todolist.ui.widget.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TodoTabRow(
    allScreens: List<TodoDestination>,
    onTabSelected: (TodoDestination) -> Unit,
    currentScreen: TodoDestination
) {
    Row(
        modifier = Modifier
            .height(TabHeight)
            .fillMaxWidth()
            .selectableGroup()
    ) {
        allScreens.forEach { screen ->
            TodoTabRowItem(
                text = screen.route,
                icon = screen.icon,
                onSelected = { onTabSelected(screen) },
                selected = currentScreen == screen
            )
        }
    }
}

val TabHeight = 56.dp