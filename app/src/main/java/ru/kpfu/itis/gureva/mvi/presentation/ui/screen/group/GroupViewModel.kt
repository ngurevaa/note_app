package ru.kpfu.itis.gureva.mvi.presentation.ui.screen.group

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import ru.kpfu.itis.gureva.mvi.presentation.ui.Route
import javax.inject.Inject

class GroupViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val id = savedStateHandle.toRoute<Route.Group>().id

    fun getId(): Int = id
}
