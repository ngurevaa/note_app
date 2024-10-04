package ru.kpfu.itis.gureva.mvi.presentation.ui.screen.group

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.kpfu.itis.gureva.mvi.R
import ru.kpfu.itis.gureva.mvi.presentation.ui.noRippleClickable

@Preview
@Composable
fun GroupScreen(
    viewModel: GroupScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val eventHandler = viewModel::obtainEvent

    GroupScreenContent(state, eventHandler)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupScreenContent(
    state: GroupScreenState,
    eventHandler: (GroupScreenEvent) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(
                    text = state.groupName,
                    style = MaterialTheme.typography.headlineMedium
                ) },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.Clear, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
         bottomBar = {
             CreateTaskButton(eventHandler)
         }
    ) { innerPadding ->
    }

    if (state.showCreateTaskBottomSheet) {
        CreateTaskBottomSheet()
    }
}

@Composable
fun CreateTaskButton(
    eventHandler: (GroupScreenEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable {
                eventHandler(GroupScreenEvent.OnTaskCreateClicked)
            }
    ) {
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(3.dp)
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }

            Spacer(modifier = Modifier.width(24.dp))

            Text(
                text = stringResource(id = R.string.btn_create_new_task),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
