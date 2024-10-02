package ru.kpfu.itis.gureva.mvi.presentation.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kpfu.itis.gureva.mvi.R
import ru.kpfu.itis.gureva.mvi.data.database.repository.GroupRepository
import ru.kpfu.itis.gureva.mvi.util.ResourceManager
import javax.inject.Inject

sealed interface MainBottomSheetEvent {
    data class OnGroupSaveClicked(val name: String): MainBottomSheetEvent
}

sealed class MainBottomSheetAction(val id: Int) {
    class ShowError(id: Int, val message: String) : MainBottomSheetAction(id)
    data object CloseBottomSheet: MainBottomSheetAction(1)
}

@HiltViewModel
class MainBottomSheetViewModel @Inject constructor(
    private val resourceManager: ResourceManager,
    private val groupRepository: GroupRepository
) : ViewModel() {
    private val _action = MutableStateFlow<MainBottomSheetAction?>(null)
    val action: StateFlow<MainBottomSheetAction?>
        get() = _action.asStateFlow()

    private var groupNameErrorCount = 0

    fun obtainEvent(event: MainBottomSheetEvent) {
        when (event) {
            is MainBottomSheetEvent.OnGroupSaveClicked -> onGroupSaveClicked(event.name)
        }
    }

    private fun onGroupSaveClicked(name: String) {
        viewModelScope.launch {
            if (name.trim().isEmpty()) {
                _action.update { MainBottomSheetAction.ShowError(groupNameErrorCount++, resourceManager.getString(
                    R.string.empty_group_name_error)) }
            }
            else if (groupRepository.getByName(name) != null) {
                _action.update { MainBottomSheetAction.ShowError(groupNameErrorCount++, resourceManager.getString(
                    R.string.existing_group_name_error)) }
            }
            else {
                groupRepository.add(name)
                _action.update { MainBottomSheetAction.CloseBottomSheet }
            }
        }
    }
}
