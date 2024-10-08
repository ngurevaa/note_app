package ru.kpfu.itis.gureva.mvi.presentation.ui.screen.group

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.kpfu.itis.gureva.mvi.R
import ru.kpfu.itis.gureva.mvi.presentation.ui.screen.main.MainBottomSheetAction
import ru.kpfu.itis.gureva.mvi.util.CalendarUtil
import ru.kpfu.itis.gureva.mvi.util.ResourceManager
import java.util.Calendar
import javax.inject.Inject

data class CreateTaskBottomSheetState(
    val task: String = "",
    val showCalendar: Boolean = false,
    val selectedDay: Calendar? = null
)

sealed interface CreateTaskBottomSheetEvent {
    data class OnTaskChanged(val task: String): CreateTaskBottomSheetEvent
    data object OnCalendarClicked : CreateTaskBottomSheetEvent
    data class OnDateSelected(val calendar: Calendar?) : CreateTaskBottomSheetEvent
    data object OnCalendarClose : CreateTaskBottomSheetEvent
    data object OnTaskSaveClicked : CreateTaskBottomSheetEvent
}

sealed class CreateTaskBottomSheetAction(val id: Int) {
    class ShowError(id: Int, val error: String) : CreateTaskBottomSheetAction(id)
}

@HiltViewModel
class CreateTaskBottomSheetViewModel @Inject constructor(
    val calendar: CalendarUtil,
    private val resourceManager: ResourceManager
) : ViewModel() {
    private val _state = MutableStateFlow(CreateTaskBottomSheetState())
    val state: StateFlow<CreateTaskBottomSheetState>
        get() = _state.asStateFlow()

    private val _action = MutableStateFlow<CreateTaskBottomSheetAction?>(null)
    val action: StateFlow<CreateTaskBottomSheetAction?>
        get() = _action.asStateFlow()

    private var taskErrorCount = 0

    fun obtainEvent(event: CreateTaskBottomSheetEvent) {
        when (event) {
            is CreateTaskBottomSheetEvent.OnTaskChanged -> onTaskChanged(event.task)
            is CreateTaskBottomSheetEvent.OnCalendarClicked -> onCalendarClicked()
            is CreateTaskBottomSheetEvent.OnCalendarClose -> onCalendarClose()
            is CreateTaskBottomSheetEvent.OnDateSelected -> onDateSelected(event.calendar)
            is CreateTaskBottomSheetEvent.OnTaskSaveClicked -> onTaskSaveClicked()
        }
    }

    private fun onTaskChanged(task: String) {
        _state.update { it.copy(task = task) }
    }

    private fun onCalendarClicked() {
        _state.update { it.copy(showCalendar = true) }
    }

    private fun onCalendarClose() {
        _state.update { it.copy(showCalendar = false) }
    }

    private fun onDateSelected(calendar: Calendar?) {
        _state.update { it.copy(selectedDay = calendar) }
    }
    private fun onTaskSaveClicked() {
        if (_state.value.task.isEmpty()) {
            _action.update { CreateTaskBottomSheetAction.ShowError(taskErrorCount, resourceManager.getString(R.string.empty_task_error)) }
        }
    }
}
