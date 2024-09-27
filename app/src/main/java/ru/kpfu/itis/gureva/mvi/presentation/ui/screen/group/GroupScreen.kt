package ru.kpfu.itis.gureva.mvi.presentation.ui.screen.group

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun GroupScreen(viewModel: GroupViewModel = hiltViewModel()) {
    Column {
        Text(text = "group${viewModel.getId()}")
    }
}
