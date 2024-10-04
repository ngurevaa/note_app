package ru.kpfu.itis.gureva.mvi.presentation.ui.screen.calendar

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kpfu.itis.gureva.mvi.presentation.ui.theme.MviTheme
import java.util.Calendar

@Preview(showBackground = true)
@Composable
fun Calendar(
//    viewModel: CreateTaskBottomSheetViewModel = hiltViewModel()
) {
    MviTheme {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Weekdays(viewModel.calendar.getWeekdays())
            val currentDate = remember { mutableStateOf(Calendar.getInstance()) }
            Month(currentDate)
            Weekdays()
            CalendarPager(currentDate)
        }
    }
}

@Composable
fun Weekdays(weekdays: List<Char> = listOf('П', 'В', 'С', 'Ч', 'П', 'С', 'В')) {
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
fun CalendarPager(currentDate: MutableState<Calendar>) {
    val initialMonth by remember { mutableStateOf(Calendar.getInstance()) }

    val pagerState = rememberPagerState(initialPage = Int.MAX_VALUE / 2, pageCount = { Int.MAX_VALUE })
    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            val month = initialMonth.clone() as Calendar
            month.add(Calendar.MONTH, page - Int.MAX_VALUE / 2)
            Log.e("l", "page $page")
            currentDate.value = month
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth()
    ) { page ->
        Column {
            val month = initialMonth.clone() as Calendar
            month.add(Calendar.MONTH, page - Int.MAX_VALUE / 2)

            val daysInMonth = month.getActualMaximum(Calendar.DAY_OF_MONTH)
            var startingDayOfWeek = (month.clone() as Calendar).also { it.set(Calendar.DAY_OF_MONTH, 1) }.get(Calendar.DAY_OF_WEEK) - 2

            var dayCount = 1
            var rowCount = 0

            while (dayCount <= daysInMonth) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)) {
                    repeat(startingDayOfWeek) {
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    for (i in 1..(7 - startingDayOfWeek)) {
                        if (dayCount <= daysInMonth) {
                            val color = if (i + startingDayOfWeek == 6 || i + startingDayOfWeek == 7)
                                MaterialTheme.colorScheme.onSurfaceVariant
                            else MaterialTheme.colorScheme.onBackground

                            Day(dayCount++, color)
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
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
fun RowScope.Day(dayCount: Int, color: Color) {
    Box(
        modifier = Modifier
            .weight(1f),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = dayCount.toString(),
            style = MaterialTheme.typography.bodySmall,
            color = color
        )
    }
}

@Composable 
fun Month(currentDate: MutableState<Calendar>) {
    val month = currentDate.value.get(Calendar.MONTH)
    val year = currentDate.value.get(Calendar.YEAR)

    val text = if (year != Calendar.getInstance().get(Calendar.YEAR)) "$month $year"
        else "$month"
    Text(
        text = text,
        modifier = Modifier.padding(24.dp)
    )
}
