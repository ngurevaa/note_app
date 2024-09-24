package ru.kpfu.itis.gureva.mvi.util

import android.content.Context
import ru.kpfu.itis.gureva.mvi.R
import java.util.Calendar
import javax.inject.Inject

class CalendarUtil @Inject constructor(
    private val resourceManager: ResourceManager
) {
    fun getWeekday(): String {
        return when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> resourceManager.getString(R.string.monday)
            Calendar.TUESDAY -> resourceManager.getString(R.string.tuesday)
            Calendar.WEDNESDAY -> resourceManager.getString(R.string.wednesday)
            Calendar.THURSDAY -> resourceManager.getString(R.string.thursday)
            Calendar.FRIDAY -> resourceManager.getString(R.string.friday)
            Calendar.SATURDAY -> resourceManager.getString(R.string.saturday)
            Calendar.SUNDAY -> resourceManager.getString(R.string.sunday)
            else -> throw IllegalArgumentException("unknown weekday")
        }
    }

    fun getDate(): String {
       return "${Calendar.getInstance().get(Calendar.DAY_OF_MONTH)} ${getMonth()}"
    }

    fun getMonth(): String {
        return when (Calendar.getInstance().get(Calendar.MONTH)) {
            Calendar.JANUARY -> resourceManager.getString(R.string.january)
            Calendar.FEBRUARY -> resourceManager.getString(R.string.february)
            Calendar.MARCH -> resourceManager.getString(R.string.march)
            Calendar.APRIL -> resourceManager.getString(R.string.april)
            Calendar.MAY -> resourceManager.getString(R.string.may)
            Calendar.JUNE -> resourceManager.getString(R.string.june)
            Calendar.JULY -> resourceManager.getString(R.string.july)
            Calendar.AUGUST -> resourceManager.getString(R.string.august)
            Calendar.SEPTEMBER -> resourceManager.getString(R.string.september)
            Calendar.OCTOBER -> resourceManager.getString(R.string.october)
            Calendar.NOVEMBER -> resourceManager.getString(R.string.november)
            Calendar.DECEMBER -> resourceManager.getString(R.string.december)
            else -> throw IllegalArgumentException("unknown month")
        }
    }
}

