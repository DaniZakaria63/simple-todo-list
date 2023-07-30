package com.daniza.simple.todolist.ui.setting

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.daniza.simple.todolist.ui.main.MainViewModel

@Composable
fun AnalyticScreen(
    viewModel: MainViewModel
){
    Surface {
        Box(contentAlignment = Alignment.Center){
            Text(text = "Still under construction")
        }
    }
}