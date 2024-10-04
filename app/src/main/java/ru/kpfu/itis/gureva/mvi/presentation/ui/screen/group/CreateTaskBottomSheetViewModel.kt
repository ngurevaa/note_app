package ru.kpfu.itis.gureva.mvi.presentation.ui.screen.group

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.kpfu.itis.gureva.mvi.util.CalendarUtil
import javax.inject.Inject

@HiltViewModel
class CreateTaskBottomSheetViewModel @Inject constructor(
    val calendar: CalendarUtil
) : ViewModel() {
}
