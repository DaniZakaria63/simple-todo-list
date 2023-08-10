package com.daniza.simple.todolist.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.daniza.simple.todolist.ui.theme.TodoTheme
import com.daniza.simple.todolist.ui.widget.navigation.MainDestination
import com.daniza.simple.todolist.ui.widget.navigation.TodoDestination
import com.daniza.simple.todolist.ui.widget.navigation.TodoNavHost
import com.daniza.simple.todolist.ui.widget.navigation.TodoTabRow
import com.daniza.simple.todolist.ui.widget.navigation.navigateSingleTopTo
import com.daniza.simple.todolist.ui.widget.navigation.todoTabRowScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainViewModel: MainViewModel by viewModels { MainViewModel.Factory }

        setContent {
            TodoTheme {
                MainActivityModule(mainViewModel)
            }
        }
    }
}

@Composable
fun MainActivityModule(
    mainViewModel: MainViewModel = viewModel()
) {
    Surface(color = MaterialTheme.colorScheme.surface, tonalElevation = 5.dp) {

        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination

        // extract current screen state for top bar animation
        var currentScreen: TodoDestination =
            todoTabRowScreen.find { it.route == currentDestination?.route } ?: MainDestination

        Scaffold(
            topBar = {
                TodoTabRow(allScreens = todoTabRowScreen, onTabSelected = { screen ->
                    navController.navigateSingleTopTo(screen.route)
                }, currentScreen = currentScreen)
            }
        ) { innerPadding ->
            TodoNavHost(
                navController = navController,
                mainViewModel = mainViewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}