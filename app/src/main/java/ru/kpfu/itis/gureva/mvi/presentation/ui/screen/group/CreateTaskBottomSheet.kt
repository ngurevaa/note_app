package ru.kpfu.itis.gureva.mvi.presentation.ui.screen.group

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ru.kpfu.itis.gureva.mvi.R
import ru.kpfu.itis.gureva.mvi.presentation.ui.noRippleClickable
import ru.kpfu.itis.gureva.mvi.presentation.ui.screen.calendar.Calendar
import ru.kpfu.itis.gureva.mvi.presentation.ui.screen.component.BottomSheetDragHandle
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

    CreateTaskBottomSheetContent(screenEventHandler, sheetEventHandler)

    if (sheetState.showCalendar) {
        CalendarBottomSheet(sheetEventHandler, sheetViewModel.calendar)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarBottomSheet(
    eventHandler: (CreateTaskBottomSheetEvent) -> Unit,
    calendar: CalendarUtil
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { eventHandler(CreateTaskBottomSheetEvent.OnCalendarClose) },
        shape = RoundedCornerShape(8.dp),
        dragHandle = {}
    ) {
        Calendar(calendar)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskBottomSheetContent(
    screenEventHandler: (GroupScreenEvent) -> Unit,
    sheetEventHandler: (CreateTaskBottomSheetEvent) -> Unit
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
        val task = remember { mutableStateOf("") }
        TaskField(task)
        TaskSettings(sheetEventHandler)
    }
}

@Composable
fun TaskField(task: MutableState<String>) {
    val focusRequester = remember { FocusRequester() }

    BasicTextField(
        value = task.value,
        onValueChange = {
            if (it.length == 1 && it[0] == ' ') return@BasicTextField
            task.value = it
        },
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
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
    sheetEventHandler: (CreateTaskBottomSheetEvent) -> Unit
) {
    Row(
        Modifier
            .padding(24.dp)) {
        Icon(
            painter = painterResource(id = R.drawable.calendar_svgrepo_com),
            contentDescription = null,
            modifier = Modifier
                .size(54.dp)
                .padding(horizontal = 16.dp)
                .noRippleClickable {
                    sheetEventHandler(CreateTaskBottomSheetEvent.OnCalendarClicked)
                }
        )

        Icon(
            painter = painterResource(id = R.drawable.notification_off_bell_alarm_svgrepo_com),
            contentDescription = null,
            modifier = Modifier
                .size(54.dp)
                .padding(horizontal = 16.dp)
        )

        Icon(
            painter = painterResource(id = R.drawable.repeat_play_svgrepo_com),
            contentDescription = null,
            modifier = Modifier
                .size(54.dp)
                .padding(horizontal = 16.dp)
        )

        Icon(
            painter = painterResource(id = R.drawable.icons8_google_docs),
            contentDescription = null,
            modifier = Modifier
                .size(54.dp)
                .padding(horizontal = 16.dp)
        )
    }
}
