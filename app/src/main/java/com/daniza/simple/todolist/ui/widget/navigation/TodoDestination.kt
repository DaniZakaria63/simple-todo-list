package com.daniza.simple.todolist.ui.widget.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Task
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

interface TodoDestination{
    val icon : ImageVector
    val route: String
}

object MainDestination: TodoDestination{
    override val icon: ImageVector = Icons.Filled.Home
    override val route: String  = "home"
}

object AnalysisDestination: TodoDestination{
    override val icon: ImageVector = Icons.Filled.BarChart
    override val route: String = "analysis"
}

object SingleTaskDestination: TodoDestination{
    override val icon: ImageVector = Icons.Filled.Task
    override val route: String = "single_task"
    const val taskTypeArgs = "task_type"
    val routeWithArgs = "$route/{$taskTypeArgs}"
    val arguments = listOf(
        navArgument(taskTypeArgs) { type = NavType.IntType }
    )
    val deepLink = listOf<NavDeepLink>(
        navDeepLink { uriPattern = "todolist://$route/{$taskTypeArgs}" }
    )
}

// Screen to be displayed in top todolist navigation
val todoTabRowScreen = listOf(MainDestination, AnalysisDestination)