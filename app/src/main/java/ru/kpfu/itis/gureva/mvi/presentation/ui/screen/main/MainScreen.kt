package ru.kpfu.itis.gureva.mvi.presentation.ui.screen.main

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ru.kpfu.itis.gureva.mvi.data.database.entity.GroupEntity
import ru.kpfu.itis.gureva.mvi.presentation.ui.theme.MviTheme
import ru.kpfu.itis.gureva.mvi.presentation.ui.theme.bodyFontFamily
import ru.kpfu.itis.gureva.mvi.R
import ru.kpfu.itis.gureva.mvi.presentation.ui.noRippleClickable

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel(), navigateToGroup: (Int?) -> Unit) {
    MviTheme {
        val state by viewModel.state.collectAsStateWithLifecycle()

        MainScreenContent(state, viewModel::obtainEvent, navigateToGroup)
    }
}

@Composable
fun MainScreenContent(
    uiState: MainScreenState,
    eventHandler: (MainScreenEvent) -> Unit,
    navigateToGroup: (Int?) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(top = 24.dp)
        ) {
            TopMainScreen(uiState = uiState, modifier = Modifier.padding(horizontal = 24.dp))
            BottomMainScreen(uiState, eventHandler, navigateToGroup, Modifier.padding(horizontal = 16.dp))

            if (uiState.showBottomSheet) {
                MainBottomSheet()
            }
        }
    }
}

@Composable
fun TopMainScreen(uiState: MainScreenState, modifier: Modifier = Modifier) {
    AnimatedVisibility(
        visible = uiState.expanded,
        enter = expandVertically() + slideInVertically(),
        exit = shrinkVertically() + slideOutVertically()
    ) {
        Column(
            modifier = modifier
        ) {
            Weekday(weekday = uiState.weekday)
            Spacer(modifier = Modifier.height(8.dp))
            CurrentDate(date = uiState.date)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun BottomMainScreen(
    uiState: MainScreenState,
    eventHandler: (MainScreenEvent) -> Unit,
    navigateToGroup: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        modifier = modifier
    ) {
        item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Search(uiState, eventHandler)
            }
        }

        items(
            items = uiState.groups
        ) { item ->
            Group(item, navigateToGroup)
        }

        item {
            AddGroup(eventHandler)
        }
    }
}

@Composable
fun AddGroup(eventHandler: (MainScreenEvent) -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Surface(
            color = MaterialTheme.colorScheme.secondary
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .noRippleClickable {
                        eventHandler(MainScreenEvent.OnGroupCreateClicked)
                    },
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }
    }
}

@Composable
fun Group(item: GroupEntity, navigateToGroup: (Int?) -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(
            contentAlignment = Alignment.BottomStart,
            modifier = Modifier
                .fillMaxSize()
                .noRippleClickable {
                    navigateToGroup(item.id)
                }
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(20.dp)
            )
        }
    }
}

@Composable
fun RowScope.Search(uiState: MainScreenState, eventHandler: (MainScreenEvent) -> Unit) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    BasicTextField(
        value = uiState.searchState,
        onValueChange = { value ->
            eventHandler(MainScreenEvent.OnSearchChanged(value))
        },
        modifier = Modifier
            .focusRequester(focusRequester)
            .onFocusChanged {
                eventHandler(MainScreenEvent.OnFocusChanged(!it.isFocused))
            }
            .weight(1f),
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .animateContentSize()
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerHighest,
                        RoundedCornerShape(percent = 30)
                    )
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.width(8.dp))
                Box(Modifier.weight(1f)) {
                    innerTextField()
                }

                AnimatedVisibility(
                    visible = uiState.searchState.isNotEmpty(),
                    enter = fadeIn(tween(durationMillis = 350)),
                    exit = fadeOut(tween(durationMillis = 350))
                ) {
                    Row {
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = null,
                            modifier = Modifier.noRippleClickable {
                                eventHandler(MainScreenEvent.OnCanselClicked)
                            }
                        )
                    }
                }
            }
        },
        textStyle = TextStyle(fontFamily = bodyFontFamily, fontSize = 18.sp)
    )

    AnimatedVisibility(
        visible = !uiState.expanded,
        enter = expandHorizontally(expandFrom = Alignment.Start),
        exit = shrinkHorizontally(shrinkTowards = Alignment.Start)
    ) {
        Row {
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(id = R.string.cansel), modifier = Modifier
                .noRippleClickable {
                    eventHandler(MainScreenEvent.OnCanselClicked)
                    focusManager.clearFocus()
                }
            )
        }
    }
}

@Composable
fun Weekday(weekday: String) {
    Text(
        text = weekday,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
fun CurrentDate(date: String) {
    Text(
        text = date,
        style = MaterialTheme.typography.headlineLarge
    )
}
