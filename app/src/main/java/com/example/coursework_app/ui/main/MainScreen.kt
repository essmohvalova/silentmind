package com.example.coursework_app.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.coursework_app.R
import com.example.coursework_app.ui.components.AppBarTitle
import com.example.coursework_app.ui.navigation.Routes

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppBarTitle(
            title = "Главная",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        MainScreenContent(
            uiState = uiState,
            onRefreshClick = { viewModel.loadData() },
            onEmotionClick = {
                navController.navigate(Routes.EMOTION_GRAPH)
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun MainScreenContent(
    uiState: MainUiState,
    onRefreshClick: () -> Unit,
    onEmotionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Привет, ${uiState.userName}!",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Button(onClick = onEmotionClick) {
            Text("Записать настроение")
        }

        Button(onClick = onRefreshClick) {
            Text("Обновить")
        }

    }
}