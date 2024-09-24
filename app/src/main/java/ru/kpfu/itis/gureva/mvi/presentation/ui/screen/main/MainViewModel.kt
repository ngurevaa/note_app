package ru.kpfu.itis.gureva.mvi.presentation.ui.screen.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.kpfu.itis.gureva.mvi.data.GroupEntity
import javax.inject.Inject

data class MainScreenState(
    val weekday: String = "ПОНЕДЕЛЬНИК",
    val date: String = "17 сентября",
    val groups: List<GroupEntity> = listOf(
        GroupEntity(1, "Сегодня"),
        GroupEntity(2, "Входящие"),
        GroupEntity(3, "Входящие"),
        GroupEntity(4, "Входящие"),
        GroupEntity(5, "Входящие"),
        GroupEntity(6, "Входящие"),
        GroupEntity(7, "Входящие")),
    val searchState: String = "",
    val expanded: Boolean = true
)

sealed interface MainScreenEvent {
    data class OnSearchChanged(val search: String) : MainScreenEvent
    data class OnFocusChanged(val focus: Boolean) : MainScreenEvent
    data object OnCanselClicked : MainScreenEvent
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val name: String
) : ViewModel() {
    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState>
        get() = _state.asStateFlow()

    fun obtainEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.OnSearchChanged -> onSearchChanged(event.search)
            is MainScreenEvent.OnFocusChanged -> onFocusChanged(event.focus)
            MainScreenEvent.OnCanselClicked -> onCanselClicked()
        }
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
