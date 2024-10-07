package ru.kpfu.itis.gureva.mvi.util

import ru.kpfu.itis.gureva.mvi.R
import java.util.Calendar
import javax.inject.Inject

class CalendarUtil @Inject constructor(
    private val resourceManager: ResourceManager
) {
    fun getWeekday(day: Int = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)): String {
        return when (day) {
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

    private fun getMonth(): String {
        return when (Calendar.getInstance().get(Calendar.MONTH)) {
            Calendar.JANUARY -> resourceManager.getString(R.string.january_date)
            Calendar.FEBRUARY -> resourceManager.getString(R.string.february_date)
            Calendar.MARCH -> resourceManager.getString(R.string.march_date)
            Calendar.APRIL -> resourceManager.getString(R.string.april_date)
            Calendar.MAY -> resourceManager.getString(R.string.may_date)
            Calendar.JUNE -> resourceManager.getString(R.string.june_date)
            Calendar.JULY -> resourceManager.getString(R.string.july_date)
            Calendar.AUGUST -> resourceManager.getString(R.string.august_date)
            Calendar.SEPTEMBER -> resourceManager.getString(R.string.september_date)
            Calendar.OCTOBER -> resourceManager.getString(R.string.october_date)
            Calendar.NOVEMBER -> resourceManager.getString(R.string.november_date)
            Calendar.DECEMBER -> resourceManager.getString(R.string.december_date)
            else -> throw IllegalArgumentException("unknown month")
        }
    }

    fun getMonth(month: Int): String {
        return when (month) {
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

    fun getWeekdays(): List<Char> {
        return mutableListOf<Char>().also {
            it.add(getWeekday(Calendar.MONDAY)[0])
            it.add(getWeekday(Calendar.TUESDAY)[0])
            it.add(getWeekday(Calendar.WEDNESDAY)[0])
            it.add(getWeekday(Calendar.THURSDAY)[0])
            it.add(getWeekday(Calendar.FRIDAY)[0])
            it.add(getWeekday(Calendar.SATURDAY)[0])
            it.add(getWeekday(Calendar.SUNDAY)[0])
        }
    }
}

