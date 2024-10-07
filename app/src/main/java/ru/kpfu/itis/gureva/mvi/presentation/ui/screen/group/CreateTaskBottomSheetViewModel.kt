package ru.kpfu.itis.gureva.mvi.presentation.ui.screen.group

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.kpfu.itis.gureva.mvi.util.CalendarUtil
import javax.inject.Inject

data class CreateTaskBottomSheetState(
    val task: String = "",
    val showCalendar: Boolean = false
)

sealed interface CreateTaskBottomSheetEvent {
    data object OnCalendarClicked : CreateTaskBottomSheetEvent
    data object OnCalendarClose : CreateTaskBottomSheetEvent
}

@HiltViewModel
class CreateTaskBottomSheetViewModel @Inject constructor(
    val calendar: CalendarUtil
) : ViewModel() {
    private val _state = MutableStateFlow(CreateTaskBottomSheetState())
    val state: StateFlow<CreateTaskBottomSheetState>
        get() = _state.asStateFlow()

    fun obtainEvent(event: CreateTaskBottomSheetEvent) {
        when (event) {
            CreateTaskBottomSheetEvent.OnCalendarClicked -> onCalendarClicked()
            CreateTaskBottomSheetEvent.OnCalendarClose -> onCalendarClose()
        }
    }

    private fun onCalendarClicked() {
        _state.update { it.copy(showCalendar = true) }
    }

    private fun onCalendarClose() {
        _state.update { it.copy(showCalendar = false) }
    }
}
