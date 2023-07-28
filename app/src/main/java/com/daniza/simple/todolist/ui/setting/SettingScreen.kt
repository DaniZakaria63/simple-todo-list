package com.daniza.simple.todolist.ui.setting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.daniza.simple.todolist.ui.main.MainViewModel

@Composable
fun SettingScreen(
    viewModel: MainViewModel
){
    Surface {
        Box(contentAlignment = Alignment.Center){
            Text(text = "Still under construction")
        }
    }
}