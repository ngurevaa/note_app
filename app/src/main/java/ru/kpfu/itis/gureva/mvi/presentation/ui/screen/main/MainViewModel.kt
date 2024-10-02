package ru.kpfu.itis.gureva.mvi.presentation.ui.screen.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kpfu.itis.gureva.mvi.R
import ru.kpfu.itis.gureva.mvi.data.database.entity.GroupEntity
import ru.kpfu.itis.gureva.mvi.data.database.repository.GroupRepository
import ru.kpfu.itis.gureva.mvi.util.CalendarUtil
import ru.kpfu.itis.gureva.mvi.util.ResourceManager
import javax.inject.Inject

data class MainScreenState(
    val weekday: String = "",
    val date: String = "",
    val groups: List<GroupEntity> = listOf(),
    val searchState: String = "",
    val expanded: Boolean = true,
    val showBottomSheet: Boolean = false
)

sealed interface MainScreenEvent {
    data class OnSearchChanged(val search: String) : MainScreenEvent
    data class OnFocusChanged(val focus: Boolean) : MainScreenEvent
    data object OnCanselClicked : MainScreenEvent
    data object OnGroupCreateClicked: MainScreenEvent
    data object OnBottomSheetClose: MainScreenEvent
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    calendar: CalendarUtil
) : ViewModel() {

    private val _state = MutableStateFlow(
        MainScreenState(
            weekday = calendar.getWeekday().uppercase(),
            date = calendar.getDate()
        )
    )
    val state: StateFlow<MainScreenState>
        get() = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(groups = groupRepository.getAll())
            }
        }
    }

    fun obtainEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.OnSearchChanged -> onSearchChanged(event.search)
            is MainScreenEvent.OnFocusChanged -> onFocusChanged(event.focus)
            is MainScreenEvent.OnCanselClicked -> onCanselClicked()
            is MainScreenEvent.OnGroupCreateClicked -> onGroupCreateClicked()
            is MainScreenEvent.OnBottomSheetClose -> onBottomSheetClose()
        }
    }

    private fun onBottomSheetClose() {
        viewModelScope.launch {
            _state.update {
                it.copy(groups = groupRepository.getAll(), showBottomSheet = false)
            }
        }
    }


    private fun onGroupCreateClicked() {
        _state.update { it.copy(showBottomSheet = true) }
    }

    private fun onSearchChanged(search: String) {
        _state.update { it.copy(searchState = search) }
    }

    private fun onFocusChanged(focus: Boolean) {
        _state.update { it.copy(expanded = focus) }
    }

    private fun onCanselClicked() {
        _state.update { it.copy(searchState = "") }
    }
}
