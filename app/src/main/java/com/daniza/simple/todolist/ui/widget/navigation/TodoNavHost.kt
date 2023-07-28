package com.daniza.simple.todolist.ui.widget.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.daniza.simple.todolist.ui.main.MainScreen
import com.daniza.simple.todolist.ui.main.MainViewModel
import com.daniza.simple.todolist.ui.setting.SettingScreen
import com.daniza.simple.todolist.ui.single_task.SingleTaskScreen

@Composable
fun TodoNavHost(
    navController: NavHostController,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = MainDestination.route,
        modifier = modifier
    ) {
        composable(route = MainDestination.route) {
            MainScreen(viewModel = viewModel)
//            navController.navigateSingleTopTo(MainDestination.route)
        }

        composable(route = SettingDestination.route){
            SettingScreen(viewModel = viewModel)
//            navController.navigateSingleTopTo(SettingDestination.route)
        }

        composable(route = SingleTaskDestination.route){
            SingleTaskScreen(viewModel = viewModel)
//            navController.navigateSingleTopTo()
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { launchSingleTop = true }

private fun NavHostController.navigateToSingleTask(taskType: String) =
    this.navigateSingleTopTo("${SingleTaskDestination.route}/$taskType")