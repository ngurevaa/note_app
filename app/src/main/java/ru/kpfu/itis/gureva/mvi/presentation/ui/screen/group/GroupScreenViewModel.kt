package ru.kpfu.itis.gureva.mvi.presentation.ui.screen.group

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kpfu.itis.gureva.mvi.data.database.entity.TaskEntity
import ru.kpfu.itis.gureva.mvi.data.database.repository.GroupRepository
import ru.kpfu.itis.gureva.mvi.presentation.ui.Route
import ru.kpfu.itis.gureva.mvi.presentation.ui.screen.main.MainScreenState
import javax.inject.Inject

data class GroupScreenState(
    val groupName: String = "",
    val tasks: List<TaskEntity> = listOf(),
    val showCreateTaskBottomSheet: Boolean = false
)

sealed interface GroupScreenEvent {
    data object OnTaskCreateClicked : GroupScreenEvent
    data object OnTackCreateClose : GroupScreenEvent
}

@HiltViewModel
class GroupScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val groupRepository: GroupRepository
) : ViewModel() {
    private val id = savedStateHandle.toRoute<Route.Group>().id

    private val _state = MutableStateFlow(GroupScreenState())
    val state: StateFlow<GroupScreenState>
        get() = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(groupName = groupRepository.getNameById(id)) }
        }
    }

    fun obtainEvent(event: GroupScreenEvent) {
        when (event) {
            is GroupScreenEvent.OnTaskCreateClicked -> onTaskCreateClicked()
            GroupScreenEvent.OnTackCreateClose -> onTaskCreateClose()
        }
    }

    private fun onTaskCreateClicked() {
        _state.update { it.copy(showCreateTaskBottomSheet = true) }
    }

    private fun onTaskCreateClose() {
        _state.update { it.copy(showCreateTaskBottomSheet = false) }
    }
}
