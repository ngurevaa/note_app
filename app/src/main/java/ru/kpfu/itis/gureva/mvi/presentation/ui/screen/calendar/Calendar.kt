package ru.kpfu.itis.gureva.mvi.presentation.ui.screen.calendar

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.kpfu.itis.gureva.mvi.presentation.ui.noRippleClickable
import ru.kpfu.itis.gureva.mvi.presentation.ui.screen.group.CreateTaskBottomSheetEvent
import ru.kpfu.itis.gureva.mvi.util.CalendarUtil
import java.util.Calendar

@Composable
fun Calendar(
    calendarUtil: CalendarUtil,
    eventHandler: (CreateTaskBottomSheetEvent) -> Unit,
    selectedDay: Calendar?
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val currentDate = remember { mutableStateOf(Calendar.getInstance()) }
        Month(currentDate, calendarUtil)
        Weekdays(calendarUtil.getWeekdays())
        CalendarPager(currentDate, eventHandler, selectedDay)
    }
}

@Composable
fun Weekdays(weekdays: List<Char>) {
    Row {
        weekdays.forEach {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = it.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun CalendarPager(
    currentDate: MutableState<Calendar>,
    eventHandler: (CreateTaskBottomSheetEvent) -> Unit,
    selectedDay: Calendar?
) {
    val initialDate by remember { mutableStateOf(Calendar.getInstance()) }
    val difference = selectedDay?.let {
        val first = initialDate.get(Calendar.YEAR) - selectedDay.get(Calendar.YEAR)
        val second =  initialDate.get(Calendar.MONTH) - selectedDay.get(Calendar.MONTH)
        first * 12 + second
    } ?: 0

    Log.e("diff", difference.toString())
    val pagerState = rememberPagerState(initialPage = Int.MAX_VALUE / 2 - difference, pageCount = { Int.MAX_VALUE })
    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            val month = initialDate.clone() as Calendar
            month.add(Calendar.MONTH, page - Int.MAX_VALUE / 2)
            currentDate.value = month
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth()
    ) { page ->
        Column {
            val date = initialDate.clone() as Calendar
            date.set(Calendar.DAY_OF_MONTH, 1)
            date.add(Calendar.MONTH, page - Int.MAX_VALUE / 2)

            val daysInMonth = date.getActualMaximum(Calendar.DAY_OF_MONTH)
            var startingDayOfWeek = ((date.clone() as Calendar).also { it.set(Calendar.DAY_OF_MONTH, 1) }.get(Calendar.DAY_OF_WEEK) + 5) % 7

            var dayCount = 1
            var rowCount = 0

            while (dayCount <= daysInMonth) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    repeat(startingDayOfWeek) {
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    for (i in 1..(7 - startingDayOfWeek)) {
                        if (dayCount <= daysInMonth) {
                            date.set(Calendar.DAY_OF_MONTH, dayCount)

                            if (date.get(Calendar.YEAR) == selectedDay?.get(Calendar.YEAR) &&
                                date.get(Calendar.MONTH) == selectedDay.get(Calendar.MONTH) &&
                                date.get(Calendar.DAY_OF_MONTH) == selectedDay.get(Calendar.DAY_OF_MONTH)
                            ) {
                                SelectedDay(
                                    dayCount = dayCount,
                                    eventHandler = eventHandler
                                )
                            }
                            else if (date.get(Calendar.YEAR) == initialDate.get(Calendar.YEAR) &&
                                date.get(Calendar.MONTH) == initialDate.get(Calendar.MONTH) &&
                                date.get(Calendar.DAY_OF_MONTH) == initialDate.get(Calendar.DAY_OF_MONTH)) {
                                Day(
                                    date = date.clone() as Calendar,
                                    color = MaterialTheme.colorScheme.error,
                                    eventHandler = eventHandler
                                )
                            }
                            else {
                                val color = if (i + startingDayOfWeek == 6 || i + startingDayOfWeek == 7)
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    else MaterialTheme.colorScheme.onBackground
                                Day(
                                    date = date.clone() as Calendar,
                                    color = color,
                                    eventHandler = eventHandler
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                        dayCount++
                    }
                    startingDayOfWeek = 0
                    rowCount++
                }
            }

            repeat(6 - rowCount) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)) {
                    Text(
                        text = "",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun RowScope.Day(
    color: Color,
    date: Calendar,
    eventHandler: (CreateTaskBottomSheetEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .padding(vertical = 12.dp)
            .noRippleClickable {
                eventHandler(CreateTaskBottomSheetEvent.OnDateSelected(date))
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.get(Calendar.DAY_OF_MONTH).toString(),
            style = MaterialTheme.typography.bodySmall,
            color = color
        )
    }
}

@Composable
fun RowScope.SelectedDay(
    dayCount: Int,
    eventHandler: (CreateTaskBottomSheetEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .noRippleClickable {
                eventHandler(CreateTaskBottomSheetEvent.OnDateSelected(null))
            },
        contentAlignment = Alignment.Center
    ) {
        val color = MaterialTheme.colorScheme.primary
        val fontSize = MaterialTheme.typography.bodySmall.fontSize
        Canvas(modifier = Modifier) {
            drawCircle(
                color = color,
                center = center,
                radius = fontSize.toPx()
            )
        }

        Text(
            text = dayCount.toString(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}


@Composable 
fun Month(currentDate: MutableState<Calendar>, calendar: CalendarUtil) {
    val month = currentDate.value.get(Calendar.MONTH)
    val year = currentDate.value.get(Calendar.YEAR)

    val text = if (year != Calendar.getInstance().get(Calendar.YEAR)) "${calendar.getMonth(month)} $year"
        else calendar.getMonth(month)
    Text(
        text = text,
        modifier = Modifier.padding(24.dp),
        style = MaterialTheme.typography.headlineMedium
    )
}

