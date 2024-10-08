package ru.kpfu.itis.gureva.mvi.presentation.ui.screen.group

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.kpfu.itis.gureva.mvi.R
import ru.kpfu.itis.gureva.mvi.presentation.ui.noRippleClickable
import ru.kpfu.itis.gureva.mvi.presentation.ui.screen.calendar.Calendar
import ru.kpfu.itis.gureva.mvi.presentation.ui.screen.component.BottomSheetDragHandle
import ru.kpfu.itis.gureva.mvi.presentation.ui.screen.main.ErrorMessage
import ru.kpfu.itis.gureva.mvi.presentation.ui.screen.main.MainBottomSheetAction
import ru.kpfu.itis.gureva.mvi.presentation.ui.screen.main.MainBottomSheetEvent
import ru.kpfu.itis.gureva.mvi.presentation.ui.screen.main.MainScreenEvent
import ru.kpfu.itis.gureva.mvi.presentation.ui.screen.main.SaveButton
import ru.kpfu.itis.gureva.mvi.presentation.ui.theme.bodyFontFamily
import ru.kpfu.itis.gureva.mvi.util.CalendarUtil

@Composable
fun CreateTaskBottomSheet(
    screenViewModel: GroupScreenViewModel = hiltViewModel(),
    sheetViewModel: CreateTaskBottomSheetViewModel = hiltViewModel()
) {
    val screenEventHandler = screenViewModel::obtainEvent
    val sheetEventHandler = sheetViewModel::obtainEvent
    val sheetState by sheetViewModel.state.collectAsStateWithLifecycle()
    val action by sheetViewModel.action.collectAsStateWithLifecycle()

    CreateTaskBottomSheetContent(screenEventHandler, sheetEventHandler, sheetState, action, sheetViewModel.calendar)

    if (sheetState.showCalendar) {
        CalendarBottomSheet(sheetEventHandler, sheetViewModel.calendar, sheetState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarBottomSheet(
    eventHandler: (CreateTaskBottomSheetEvent) -> Unit,
    calendar: CalendarUtil,
    state: CreateTaskBottomSheetState
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { eventHandler(CreateTaskBottomSheetEvent.OnCalendarClose) },
        shape = RoundedCornerShape(8.dp),
        dragHandle = {}
    ) {
        Calendar(calendar, eventHandler, state.selectedDay)

        var first by remember { mutableStateOf(true) }
        LaunchedEffect(key1 = state.selectedDay) {
            if (!first) {
                delay(200)
                eventHandler(CreateTaskBottomSheetEvent.OnCalendarClose)
            }
            else first = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskBottomSheetContent(
    screenEventHandler: (GroupScreenEvent) -> Unit,
    sheetEventHandler: (CreateTaskBottomSheetEvent) -> Unit,
    state: CreateTaskBottomSheetState,
    action: CreateTaskBottomSheetAction?,
    calendar: CalendarUtil
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { screenEventHandler(GroupScreenEvent.OnTackCreateClose) },
        shape = RoundedCornerShape(8.dp),
        dragHandle = { BottomSheetDragHandle(title = stringResource(id = R.string.new_task)) {
            scope.launch { sheetState.hide() }
                .invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        screenEventHandler(GroupScreenEvent.OnTackCreateClose)
                    }
                }
        } }
    ) {
        Column(modifier = Modifier.padding(start = 24.dp, end = 24.dp)) {
            TaskField(state.task, sheetEventHandler)
            Spacer(modifier = Modifier.height(24.dp))
            TaskSettings(sheetEventHandler, state, calendar)
            Spacer(modifier = Modifier.height(8.dp))
        }
        SaveTaskButton(sheetEventHandler, action)
    }
}

@Composable
fun TaskField(
    task: String,
    eventHandler: (CreateTaskBottomSheetEvent) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    BasicTextField(
        value = task,
        onValueChange = {
            if (it.length == 1 && it[0] == ' ') return@BasicTextField
            eventHandler(CreateTaskBottomSheetEvent.OnTaskChanged(it))
        },
        modifier = Modifier
            .focusRequester(focusRequester),
        textStyle = TextStyle(fontFamily = bodyFontFamily, fontSize = 18.sp, lineHeight = 28.sp),
        minLines = 5,
        maxLines = 5
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun TaskSettings(
    sheetEventHandler: (CreateTaskBottomSheetEvent) -> Unit,
    state: CreateTaskBottomSheetState,
    calendar: CalendarUtil
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (state.selectedDay == null) {
            Icon(
                painter = painterResource(id = R.drawable.calendar_svgrepo_com),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .size(38.dp)
                    .padding(end = 20.dp)
                    .noRippleClickable {
                        sheetEventHandler(CreateTaskBottomSheetEvent.OnCalendarClicked)
                    }
            )
        }
        else {
            Text(
                text = calendar.getFullDate(state.selectedDay),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(end = 20.dp)
                    .noRippleClickable {
                        sheetEventHandler(CreateTaskBottomSheetEvent.OnCalendarClicked)
                    }
            )
        }


        Icon(
            painter = painterResource(id = R.drawable.notification_off_bell_alarm_svgrepo_com),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .size(38.dp)
                .padding(end = 20.dp)
        )

        Icon(
            painter = painterResource(id = R.drawable.repeat_play_svgrepo_com),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .size(38.dp)
                .padding(end = 20.dp)
        )

        Icon(
            painter = painterResource(id = R.drawable.icons8_google_docs),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .size(38.dp)
                .padding(end = 20.dp)
        )
    }
}

@Composable
fun SaveTaskButton(
    eventHandler: (CreateTaskBottomSheetEvent) -> Unit,
    action: CreateTaskBottomSheetAction?
) {
    var visibility by remember {
        mutableStateOf(false)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondary
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .noRippleClickable {
                    if (!visibility) {
                        eventHandler(CreateTaskBottomSheetEvent.OnTaskSaveClicked)
                    }
                }
        ) {
            var first by remember {
                mutableStateOf(true)
            }
            LaunchedEffect(key1 = action) {
                when (action) {
                    is CreateTaskBottomSheetAction.ShowError -> {
                        if (!first) visibility = true
                        else first = false
                    }
                    else -> {}
                }
            }
            Crossfade(
                targetState = visibility,
                animationSpec = tween(1000),
                label = ""
            ) { visible ->
                if (visible) {
                    ErrorMessage(message = (action as? CreateTaskBottomSheetAction.ShowError)?.error.toString())

                    LaunchedEffect(visibility) {
                        if (visibility) {
                            delay(1500)
                            visibility = false
                        }
                    }

                }
                else {
                    SaveButton()
                }
            }
        }
    }
}
