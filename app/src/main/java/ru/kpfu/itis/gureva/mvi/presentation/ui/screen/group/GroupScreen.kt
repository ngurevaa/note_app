package ru.kpfu.itis.gureva.mvi.presentation.ui.screen.group

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun GroupScreen(id: Int) {
    Column {
        Text(text = "group$id")
    }
}
