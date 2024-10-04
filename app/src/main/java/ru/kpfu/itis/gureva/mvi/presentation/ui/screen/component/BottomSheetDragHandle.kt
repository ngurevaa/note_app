package ru.kpfu.itis.gureva.mvi.presentation.ui.screen.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.kpfu.itis.gureva.mvi.presentation.ui.noRippleClickable

@Composable
fun BottomSheetDragHandle(title: String, onCloseClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.Center)
        )

        Icon(
            Icons.Filled.Clear,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .noRippleClickable {
                    onCloseClicked()
                }
        )
    }
}
