package com.daniza.simple.todolist.ui.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.daniza.simple.todolist.data.model.StatisticsModel
import com.daniza.simple.todolist.data.source.TaskUiState
import com.daniza.simple.todolist.ui.main.MainViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnalyticScreen(
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val statisticsData: TaskUiState<StatisticsModel> by mainViewModel.allStatisticsData
        .collectAsStateWithLifecycle(initialValue = TaskUiState(isLoading = true))

    Surface(modifier = modifier.background(MaterialTheme.colorScheme.surface)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            FlowRow {
                statisticsData.dataList?.forEach { model ->
                    AnalyticCard(
                        model = model,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AnalyticCard(
    modifier: Modifier = Modifier,
    model: StatisticsModel
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        modifier = modifier
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = model.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = model.value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

    }
}