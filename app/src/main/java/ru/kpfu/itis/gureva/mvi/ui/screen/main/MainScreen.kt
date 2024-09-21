package ru.kpfu.itis.gureva.mvi.ui.screen.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.kpfu.itis.gureva.mvi.data.GroupEntity
import ru.kpfu.itis.gureva.mvi.ui.theme.MviTheme
import ru.kpfu.itis.gureva.mvi.ui.theme.bodyFontFamily
import kotlin.math.exp

@Preview
@Composable
fun MainScreen() {
    MviTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(24.dp)) {
                Weekday(weekday = "ПОНЕДЕЛЬНИК")
                CurrentDate(date = "17 сентября")
                Spacer(modifier = Modifier.height(24.dp))
                Search()
                Spacer(modifier = Modifier.height(24.dp))
                Groups()

//                var textState by remember { mutableStateOf("") }
//                var focusState by remember { mutableStateOf("") }
//
//                val localFocusManager = LocalFocusManager.current
//                val focusRequester = FocusRequester()
//
//                Text(
//                    text = focusState,
//                    fontSize = 22.sp,
//                    color = Color(0xFF0047AB)
//                )
//
//                BasicTextField(
//                    value = textState,
//                    onValueChange = { textState = it },
//                    modifier = Modifier
//                        .focusRequester(focusRequester)
//                        .fillMaxWidth()
//                        .onFocusChanged {
//                            focusState = if (it.isFocused) {
//                                "TextField is focused."
//                            } else {
//                                "TextField has no focus."
//                            }
//                        },
//                )
//
//                Row(horizontalArrangement = Arrangement.SpaceEvenly,modifier = Modifier.fillMaxWidth()) {
//                    Button(onClick = {
//                        focusRequester.requestFocus()
//                    }) {
//                        Text(text = "Set Focus")
//                    }
//
//                    Button(onClick = {
//                        localFocusManager.clearFocus()
//                    }) {
//                        Text(text = "Clear Focus")
//                    }
//                }
            }
        }
    }
}

@Composable
fun Groups(groups: List<GroupEntity> = listOf(GroupEntity(1, "Сегодня"),
    GroupEntity(2, "Входящие"))) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(
            items = groups,
            key = {group: GroupEntity ->  group.id},
        ) { item ->
            Group(item)
        }

        item {
            AddGroup()
        }
    }
}

@Composable
fun AddGroup() {
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
                    .fillMaxSize(),
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }
    }
}

@Composable
fun Group(item: GroupEntity, modifier: Modifier = Modifier) {
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
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(20.dp)
            )
        }
    }
}

@Composable
fun Search(modifier: Modifier = Modifier) {
    var text by remember {
        mutableStateOf("lal")
    }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var value by rememberSaveable { mutableStateOf("") }
    var expanded by remember {
        mutableStateOf(true)
    }
//    val interactionSource = remember { MutableInteractionSource() }
//    val isFocused by interactionSource.collectIsFocusedAsState()
//
//
//    LaunchedEffect(isFocused) {
//        text = isFocused.toString()
//        expanded = !isFocused
//    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        AnimatedVisibility(
            visible = !expanded,
            enter = expandHorizontally(expandFrom = Alignment.Start,
                animationSpec = tween(durationMillis = 2000)),
            exit = fadeOut(animationSpec = tween(durationMillis = 1100)),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Text(text = "cansel", modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable {
                    focusManager.clearFocus()
                })
        }

        BasicTextField(
            value = value,
            onValueChange = {
                value = it
            },
            modifier = Modifier
                .focusRequester(focusRequester)
                .onFocusChanged {
                    text = if (it.isFocused) {
                        "TextField is focused."
                    } else {
                        "TextField has no focus."
                    }

                    expanded = if (it.isFocused) {
                        false
                    } else {
                        true
                    }
                },
            singleLine = true,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .animateContentSize(animationSpec = tween(durationMillis = 2000))
                        .fillMaxWidth(if (expanded) 1f else 0.7f)
                        .background(
                            MaterialTheme.colorScheme.surfaceContainerHighest,
                            RoundedCornerShape(percent = 30)
                        )
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.width(8.dp))
                    innerTextField()
                }
            },
            textStyle = TextStyle(fontFamily = bodyFontFamily, fontSize = 18.sp),
            cursorBrush = SolidColor(Color.LightGray),
//            interactionSource = interactionSource,
        )


        Text(text = text, modifier = Modifier.align(Alignment.BottomStart))
    }
    
}

@Composable
fun Weekday(weekday: String) {
    Text(
        text = weekday,
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
fun CurrentDate(date: String) {
    Text(
        text = date,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(top = 8.dp)
    )
}
