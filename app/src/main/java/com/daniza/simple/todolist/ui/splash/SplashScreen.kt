package com.daniza.simple.todolist.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay


/*This should be screen for waiting while the system initializing through API
* */
@Composable
fun SplashScreen(
    onTimeout: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red),
        contentAlignment = Alignment.Center
    ) {
        val currentTimeout by rememberUpdatedState(onTimeout)
        LaunchedEffect(Unit) {
            delay(SPLASH_SCREEN_TIMEOUT)
            currentTimeout()
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.Center)) {
            Icon(imageVector = Icons.Filled.Camera, contentDescription = "---")
            Text(text = "------")
        }
    }
}
private const val SPLASH_SCREEN_TIMEOUT = 2000L